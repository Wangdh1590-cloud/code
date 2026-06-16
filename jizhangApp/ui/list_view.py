"""列表筛选 + 表格展示"""

from typing import Optional, Tuple
import streamlit as st
from config import CATEGORIES
from database import get_records, get_month_options


def render_list_view() -> Tuple[Optional[str], Optional[str]]:
    """
    渲染筛选栏 + 记录表格。
    返回 (selected_month, selected_category) 供统计组件复用。
    """
    st.subheader("📋 账目列表")

    # ---- 筛选栏 ----
    col1, col2 = st.columns(2)
    month_options = get_month_options()

    with col1:
        month_filter = st.selectbox(
            "月份筛选",
            options=["全部"] + month_options,
            key="filter_month"
        )
    with col2:
        category_filter = st.selectbox(
            "分类筛选",
            options=["全部"] + CATEGORIES,
            key="filter_category"
        )

    # ---- 查询数据 ----
    month = None if month_filter == "全部" else month_filter
    category = None if category_filter == "全部" else category_filter
    df = get_records(month=month, category=category)

    # ---- 展示 ----
    if df.empty:
        st.info("暂无记录，去侧边栏添加一笔吧")
    else:
        st.dataframe(df, use_container_width=True, hide_index=True)
        total = df["金额"].sum()
        st.caption(f"共 {len(df)} 条记录，合计 ¥{total:.2f}")

    return month, category
