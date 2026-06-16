"""按 ID 删除账目"""

import streamlit as st
from database import delete_record


def render_delete_view():
    """渲染删除操作区域"""
    st.subheader("🗑 删除账目")

    col1, col2 = st.columns([2, 1])
    with col1:
        record_id = st.number_input(
            "记录 ID",
            min_value=1,
            step=1,
            format="%d",
            key="delete_id"
        )
    with col2:
        st.write("")  # 占位对齐
        st.write("")
        confirm = st.button("删除", key="delete_btn", use_container_width=True)

    if confirm:
        try:
            success = delete_record(int(record_id))
            if success:
                st.success(f"已删除 ID={record_id} 的记录")
                st.rerun()
            else:
                st.error(f"ID={record_id} 不存在，请检查")
        except Exception as e:
            st.error(f"删除失败：{e}")
