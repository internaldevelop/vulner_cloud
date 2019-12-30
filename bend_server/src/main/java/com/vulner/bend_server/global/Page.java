package com.vulner.bend_server.global;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

    private List<T> data;

    private long page_index; // 当前页
    private long first_result; // 当前页的起始记录
    private long total_results; // 总共记录数
    private int page_size; // 每页的数量
    private long total_page; // 总共多少页
    private long next_page; // 下一页
    private long previous_page; // 上一页

    public Page() {}

    public Page(long page_index, int page_size) {
        if (page_index > 1)
            this.page_index = page_index;
        else
            this.page_index = 1;
        this.page_size = page_size;
        this.first_result = (this.page_index - 1) * this.page_size + 1;
    }

    public long getPageIndex() {
        return page_index;
    }

    public int getPageSize() {
        return page_size;
    }

    public void setPageIndex(long page_index) {
        this.page_index = page_index;
        if (page_index <= 0)
            this.page_index = 1;
        if (page_index > this.total_page)
            this.page_index = total_page;
        this.first_result = (this.page_index - 1) * this.page_size;

    }

    public long getTotalResults() {
        return total_results;
    }

    public void setTotalResults(long total_results) {
        this.total_results = total_results;
        if (total_results % this.page_size == 0) {
            this.total_page = total_results / this.page_size;
        } else {
            this.total_page = (long) Math.floor(total_results / this.page_size) + 1;
        }

        if (this.total_page == 0) {
            this.total_page = 1;
        }
        if (this.page_index > total_page) {
            this.page_index = total_page;
            this.first_result = (this.page_index - 1) * this.page_size;

        }
        if (this.page_index > 1) {
            this.previous_page = this.page_index - 1;
        } else {
            this.previous_page = 1;
        }
    }

    public long getfirstResult() {
        return first_result;
    }

    public long getNextPage() {
        if (this.page_index < this.total_page) {
            this.next_page = this.page_index + 1;
        } else {
            this.next_page = this.total_page;
        }
        return next_page;
    }

    public long getPreviousPage() {
        return previous_page;
    }

    public long getTotalPage() {
        return total_page;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Page [data=" + data + ", first_result=" + first_result
                + ", next_page=" + next_page + ", page_index=" + page_index
                + ", page_size=" + page_size + ", previous_page=" + previous_page
                + ", total_page=" + total_page + ", total_results=" + total_results
                + "]";
    }

}
