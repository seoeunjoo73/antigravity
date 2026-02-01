<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp" />

<div class="cabinet-container">
    <h2>디지털 캐비닛 - 폴더 목록</h2>

    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="create-folder-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
        <h3>새 폴더 생성</h3>
        <form action="/cabinet/folder/create" method="post">
            <div style="margin-bottom: 10px;">
                <label>폴더명:</label>
                <input type="text" name="name" required style="padding: 8px; width: 200px;">
            </div>
            <div style="margin-bottom: 10px;">
                <label>최대 파일 사이즈 (Bytes, 0은 제한없음):</label>
                <input type="number" name="maxFileSize" value="10485760" required style="padding: 8px; width: 200px;">
            </div>
            <button type="submit" class="btn btn-primary" style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">생성하기</button>
        </form>
    </div>

    <div class="folder-list">
        <h3>폴더 목록 (1 Depth)</h3>
        <table style="width: 100%; border-collapse: collapse;">
            <thead>
                <tr style="background-color: #f8f9fa;">
                    <th style="padding: 12px; border: 1px solid #dee2e6; text-align: left;">폴더명</th>
                    <th style="padding: 12px; border: 1px solid #dee2e6; text-align: left;">최대 사이즈(B)</th>
                    <th style="padding: 12px; border: 1px solid #dee2e6; text-align: center;">작업</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="folder" items="${folders}">
                    <tr>
                        <td style="padding: 12px; border: 1px solid #dee2e6;">${folder.name}</td>
                        <td style="padding: 12px; border: 1px solid #dee2e6;">${folder.maxFileSize}</td>
                        <td style="padding: 12px; border: 1px solid #dee2e6; text-align: center;">
                            <a href="/cabinet/${folder.id}" class="btn btn-info" style="padding: 5px 10px; text-decoration: none; background-color: #17a2b8; color: white; border-radius: 4px;">상세 및 권한 설정</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty folders}">
                    <tr>
                        <td colspan="3" style="padding: 20px; text-align: center; color: #666;">등록된 폴더가 없습니다.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
