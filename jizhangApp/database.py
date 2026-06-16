"""数据层 —— 所有 SQLite 操作，不涉及 Streamlit UI"""

import os
import sqlite3
from typing import Optional, List
import pandas as pd
from config import DB_PATH, CATEGORIES


def _get_conn() -> sqlite3.Connection:
    """获取数据库连接"""
    os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row  # 让查询结果支持按列名访问
    return conn


def init_db():
    """初始化数据库：建表、建索引（应用启动时调用一次）"""
    conn = _get_conn()
    conn.execute("""
        CREATE TABLE IF NOT EXISTS records (
            id         INTEGER PRIMARY KEY AUTOINCREMENT,
            amount     REAL    NOT NULL CHECK(amount > 0),
            category   TEXT    NOT NULL,
            date       TEXT    NOT NULL,
            note       TEXT    DEFAULT '',
            created_at TEXT    DEFAULT (datetime('now', 'localtime'))
        )
    """)
    conn.execute("CREATE INDEX IF NOT EXISTS idx_records_date ON records(date)")
    conn.execute("CREATE INDEX IF NOT EXISTS idx_records_category ON records(category)")
    conn.commit()
    conn.close()


def add_record(amount: float, category: str, date: str, note: str) -> int:
    """插入一条记录，返回新记录 ID"""
    conn = _get_conn()
    cursor = conn.execute(
        "INSERT INTO records (amount, category, date, note) VALUES (?, ?, ?, ?)",
        (amount, category, date, note)
    )
    conn.commit()
    record_id = cursor.lastrowid
    conn.close()
    return record_id


def get_records(month: Optional[str] = None, category: Optional[str] = None) -> pd.DataFrame:
    """
    按条件查询记录，返回 DataFrame。
    month 格式: YYYY-MM，为 None 表示不按月份筛选。
    category 为 None 表示不按分类筛选。
    """
    conn = _get_conn()
    conditions = []
    params = []

    if month:
        conditions.append("strftime('%Y-%m', date) = ?")
        params.append(month)

    if category:
        conditions.append("category = ?")
        params.append(category)

    where = ""
    if conditions:
        where = "WHERE " + " AND ".join(conditions)

    sql = f"SELECT id, amount, category, date, note FROM records {where} ORDER BY date DESC, id DESC"
    df = pd.read_sql_query(sql, conn, params=params)
    conn.close()

    if not df.empty:
        df.columns = ["ID", "金额", "分类", "日期", "备注"]

    return df


def delete_record(record_id: int) -> bool:
    """按 ID 删除记录，返回是否成功"""
    conn = _get_conn()
    cursor = conn.execute("DELETE FROM records WHERE id = ?", (record_id,))
    conn.commit()
    deleted = cursor.rowcount > 0
    conn.close()
    return deleted


def get_month_options() -> List[str]:
    """获取有记录的所有月份，降序排列，供下拉筛选"""
    conn = _get_conn()
    rows = conn.execute(
        "SELECT DISTINCT strftime('%Y-%m', date) AS month FROM records ORDER BY month DESC"
    ).fetchall()
    conn.close()
    return [row["month"] for row in rows]


def get_category_stats(month: Optional[str] = None) -> pd.DataFrame:
    """
    获取各分类统计：总金额、记录数、占比。
    month 为 None 时统计全部。
    """
    conn = _get_conn()
    if month:
        rows = conn.execute(
            """
            SELECT category, SUM(amount) AS total, COUNT(*) AS count
            FROM records
            WHERE strftime('%Y-%m', date) = ?
            GROUP BY category
            ORDER BY total DESC
            """,
            (month,)
        ).fetchall()
    else:
        rows = conn.execute(
            """
            SELECT category, SUM(amount) AS total, COUNT(*) AS count
            FROM records
            GROUP BY category
            ORDER BY total DESC
            """
        ).fetchall()
    conn.close()

    if not rows:
        return pd.DataFrame(columns=["分类", "总金额", "记录数", "占比"])

    grand_total = sum(r["total"] for r in rows)
    data = []
    for r in rows:
        data.append({
            "分类": r["category"],
            "总金额": round(r["total"], 2),
            "记录数": r["count"],
            "占比": f"{r['total'] / grand_total * 100:.1f}%"
        })
    return pd.DataFrame(data)
