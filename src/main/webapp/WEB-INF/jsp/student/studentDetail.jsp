<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- 페이지 제목 -->
<div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
    <h4 class="mb-0"><i class="bi bi-person-badge me-2"></i>학생 상세 정보</h4>
    <div>
        <a href="${pageContext.request.contextPath}/student/updateForm.do?studentId=${student.studentId}"
           class="btn btn-primary btn-sm me-1">
            <i class="bi bi-pencil me-1"></i>수정
        </a>
        <button type="button" class="btn btn-danger btn-sm me-1"
                onclick="confirmDelete('${pageContext.request.contextPath}/student/delete.do?studentId=${student.studentId}', '${student.studentName} 학생을 삭제하시겠습니까?')">
            <i class="bi bi-trash me-1"></i>삭제
        </button>
        <a href="${pageContext.request.contextPath}/student/list.do?searchType=${searchVO.searchType}&searchKeyword=${searchVO.searchKeyword}&pageIndex=${searchVO.pageIndex}&pageSize=${searchVO.pageSize}"
           class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-list me-1"></i>목록
        </a>
    </div>
</div>

<div class="card border-0 shadow-sm">
    <div class="card-body">
        <div class="row g-3">
            <div class="col-md-6">
                <table class="table table-bordered table-sm">
                    <colgroup><col style="width:35%"><col></colgroup>
                    <tr>
                        <th class="table-light">학번</th>
                        <td class="fw-bold">${student.studentNum}</td>
                    </tr>
                    <tr>
                        <th class="table-light">이름</th>
                        <td>${student.studentName}</td>
                    </tr>
                    <tr>
                        <th class="table-light">학과</th>
                        <td>${student.deptName}</td>
                    </tr>
                    <tr>
                        <th class="table-light">학년</th>
                        <td>${student.grade}학년</td>
                    </tr>
                    <tr>
                        <th class="table-light">성별</th>
                        <td>${student.gender == 'M' ? '남성' : '여성'}</td>
                    </tr>
                </table>
            </div>
            <div class="col-md-6">
                <table class="table table-bordered table-sm">
                    <colgroup><col style="width:35%"><col></colgroup>
                    <tr>
                        <th class="table-light">연락처</th>
                        <td>${student.phone}</td>
                    </tr>
                    <tr>
                        <th class="table-light">이메일</th>
                        <td><a href="mailto:${student.email}">${student.email}</a></td>
                    </tr>
                    <tr>
                        <th class="table-light">주소</th>
                        <td>${student.address}</td>
                    </tr>
                    <tr>
                        <th class="table-light">생년월일</th>
                        <td><fmt:formatDate value="${student.birthDate}" pattern="yyyy-MM-dd"/></td>
                    </tr>
                    <tr>
                        <th class="table-light">등록일</th>
                        <td><fmt:formatDate value="${student.regDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 관련 정보 바로가기 -->
<div class="row g-3 mt-2">
    <div class="col-md-6">
        <div class="card border-0 shadow-sm text-center py-3">
            <i class="bi bi-bar-chart fs-2 text-primary mb-2"></i>
            <div class="small text-muted mb-1">성적 조회</div>
            <a href="${pageContext.request.contextPath}/grade/list.do?studentId=${student.studentId}"
               class="btn btn-outline-primary btn-sm">조회하기</a>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card border-0 shadow-sm text-center py-3">
            <i class="bi bi-calendar-check fs-2 text-success mb-2"></i>
            <div class="small text-muted mb-1">출결 현황</div>
            <a href="${pageContext.request.contextPath}/attend/list.do?studentId=${student.studentId}"
               class="btn btn-outline-success btn-sm">조회하기</a>
        </div>
    </div>
</div>
