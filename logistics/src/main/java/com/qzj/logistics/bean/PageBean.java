package com.qzj.logistics.bean;

import com.qzj.logistics.base.BaseBean;

/**
 * Created by Administrator on 2016/1/4.
 */
public class PageBean extends BaseBean {

    //  总条数
    private int totalCount;
    //  总页数
    private int totalPage;
    //  一页显示条数
    private int pageCount;
    //  当前页码
    private int currentPage;
    //  下一页码
    private int nextPage;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if (totalCount <= 0) return;
        if (totalCount%pageCount == 0){
            totalPage = totalCount%pageCount;
        } else {
            totalPage = totalCount%pageCount + 1;
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNextPage() {
        if (currentPage == 0) return 1;
        if (currentPage < totalPage){
            return currentPage+1;
        }
        return totalPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isLastPage(){
        if (currentPage == totalPage) return true;
        return false;
    }
}
