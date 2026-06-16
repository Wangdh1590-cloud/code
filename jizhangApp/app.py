"""记账本主入口 —— 编排所有 UI 组件"""

import streamlit as st
from config import PAGE_TITLE, PAGE_ICON
from database import init_db
from ui.sidebar import render_add_form
from ui.list_view import render_list_view
from ui.stats_view import render_stats_view
from ui.delete_view import render_delete_view


def main():
    # 页面配置
    st.set_page_config(page_title=PAGE_TITLE, page_icon=PAGE_ICON, layout="wide")

    # 初始化数据库（只执行一次）
    init_db()

    # 侧边栏：添加账目
    render_add_form()

    # 主区域
    st.title(f"{PAGE_ICON} {PAGE_TITLE}")

    # 列表（筛选 + 表格），返回当前筛选条件供统计复用
    month, _category = render_list_view()

    # 分类统计
    st.divider()
    render_stats_view(month=month)

    # 删除
    st.divider()
    render_delete_view()


if __name__ == "__main__":
    main()
