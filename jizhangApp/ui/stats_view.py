"""分类统计 —— 柱状图 + 统计表"""

from typing import Optional
import streamlit as st
from database import get_category_stats


def render_stats_view(month: Optional[str] = None):
    """渲染分类统计图表，可指定月份筛选"""
    st.subheader("📊 分类统计")

    df = get_category_stats(month=month)

    if df.empty:
        st.info("暂无统计数据")
    else:
        # 柱状图
        chart_data = df.set_index("分类")[["总金额"]]
        st.bar_chart(chart_data, y_label="总金额（元）")

        # 统计表
        st.dataframe(df, use_container_width=True, hide_index=True)
