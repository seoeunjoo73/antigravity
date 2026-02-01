<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
            <jsp:include page="../common/header.jsp" />

            <div class="cabinet-container">
                <h2>폴더 상세: ${folder.name}</h2>
                <p><a href="/cabinet">← 목록으로 돌아가기</a></p>

                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <div style="display: flex; gap: 40px; margin-top: 30px;">
                    <!-- 파일 섹션 -->
                    <div style="flex: 1;">
                        <h3>파일 업로드 및 목록</h3>
                        <div style="padding: 20px; border: 1px solid #ddd; border-radius: 8px; margin-bottom: 20px;">
                            <form action="/cabinet/${folder.id}/upload" method="post" enctype="multipart/form-data">
                                <input type="file" name="file" required>
                                <button type="submit" class="btn btn-success" style="margin-left: 10px;">업로드</button>
                            </form>
                            <p style="font-size: 0.9em; color: #666; margin-top: 5px;">최대 크기: ${folder.maxFileSize}
                                Bytes</p>
                        </div>

                        <table style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr style="background-color: #f8f9fa;">
                                    <th style="padding: 10px; border: 1px solid #dee2e6;">파일명</th>
                                    <th style="padding: 10px; border: 1px solid #dee2e6;">크기(B)</th>
                                    <th style="padding: 10px; border: 1px solid #dee2e6;">업로드일</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="file" items="${files}">
                                    <tr>
                                        <td style="padding: 10px; border: 1px solid #dee2e6;">${file.originalFileName}
                                        </td>
                                        <td style="padding: 10px; border: 1px solid #dee2e6;">${file.fileSize}</td>
                                        <td style="padding: 10px; border: 1px solid #dee2e6;">${file.uploadedAt}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty files}">
                                    <tr>
                                        <td colspan="3" style="padding: 15px; text-align: center; color: #999;">파일이
                                            없습니다.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <!-- 권한 설정 섹션 -->
                    <div style="flex: 1;">
                        <h3>사용자 권한 설정</h3>
                        <table style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr style="background-color: #f8f9fa;">
                                    <th style="padding: 10px; border: 1px solid #dee2e6;">사용자명</th>
                                    <th style="padding: 10px; border: 1px solid #dee2e6;">역할</th>
                                    <th style="padding: 10px; border: 1px solid #dee2e6;">권한 상태</th>
                                    <th style="padding: 10px; border: 1px solid #dee2e6;">작업</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${companyUsers}">
                                    <tr>
                                        <td style="padding: 10px; border: 1px solid #dee2e6;">${user.username}</td>
                                        <td style="padding: 10px; border: 1px solid #dee2e6;">${user.role}</td>
                                        <td style="padding: 10px; border: 1px solid #dee2e6; text-align: center;">
                                            <c:set var="hasPermission" value="false" />
                                            <c:forEach var="perm" items="${permissions}">
                                                <c:if test="${perm.user.id == user.id}">
                                                    <c:set var="hasPermission" value="true" />
                                                </c:if>
                                            </c:forEach>

                                            <c:choose>
                                                <c:when test="${user.role == 'REPRESENTATIVE'}">
                                                    <span style="color: blue; font-weight: bold;">전체 권한</span>
                                                </c:when>
                                                <c:when test="${hasPermission}">
                                                    <span style="color: green; font-weight: bold;">부여됨</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #ccc;">없음</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="padding: 10px; border: 1px solid #dee2e6; text-align: center;">
                                            <c:if test="${user.role != 'REPRESENTATIVE'}">
                                                <c:choose>
                                                    <c:when test="${hasPermission}">
                                                        <form action="/cabinet/${folder.id}/permission/revoke"
                                                            method="post" style="display: inline;">
                                                            <input type="hidden" name="userId" value="${user.id}">
                                                            <button type="submit"
                                                                style="background-color: #dc3545; color: white; border: none; padding: 5px 8px; border-radius: 4px; cursor: pointer;">권한
                                                                박탈</button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form action="/cabinet/${folder.id}/permission/grant"
                                                            method="post" style="display: inline;">
                                                            <input type="hidden" name="userId" value="${user.id}">
                                                            <button type="submit"
                                                                style="background-color: #28a745; color: white; border: none; padding: 5px 8px; border-radius: 4px; cursor: pointer;">권한
                                                                부여</button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <jsp:include page="../common/footer.jsp" />