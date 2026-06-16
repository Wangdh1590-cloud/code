"""添加账目表单 —— 侧边栏"""

import streamlit as st
from config import CATEGORIES
from database import add_record


def render_add_form():
    """渲染侧边栏添加账目表单"""
    st.sidebar.subheader("📝 添加账目")

    with st.sidebar.form("add_form", clear_on_submit=True):
        amount = st.number_input(
            "金额",
            min_value=0.01,
            step=0.01,
            format="%.2f",
            help="请输入支出金额"
        )
        category = st.selectbox("分类", options=CATEGORIES)
        date = st.date_input("日期")
        note = st.text_input("备注（选填）", placeholder="例如：午餐外卖")

        submitted = st.form_submit_button("提交记录", use_container_width=True)

        if submitted:
            if amount <= 0:
                st.error("金额必须大于 0")
            else:
                try:
                    record_id = add_record(
                        amount=amount,
                        category=category,
                        date=date.strftime("%Y-%m-%d"),
                        note=note.strip()
                    )
                    st.success(f"添加成功！记录 ID: {record_id}")
                    st.rerun()
                except Exception as e:
                    st.error(f"添加失败：{e}")
