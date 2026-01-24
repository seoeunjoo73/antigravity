<%@ include file="../common/header.jsp" %>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <h2>${board.name} <small style="color: #666; font-size: 0.8em;"> - ${board.description}</small></h2>
            <a href="/board/${board.id}/write" class="btn btn-primary">글쓰기</a>
        </div>

        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th style="width: 8%">번호</th>
                        <th>제목</th>
                        <th style="width: 15%">작성자</th>
                        <th style="width: 20%">작성일</th>
                        <th style="width: 10%">첨부</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="post" items="${posts.content}">
                        <tr>
                            <td>${post.id}</td>
                            <td><a href="/board/view/${post.id}">${post.title}</a></td>
                            <td>${post.author}</td>
                            <td>${post.createdAt}</td>
                            <td>
                                <c:if test="${not empty post.attachment}">
                                    📎
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty posts.content}">
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 40px;">등록된 게시글이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <!-- Simple Pagination (Optional) -->
            <div style="margin-top: 10px;">
                <c:if test="${posts.totalPages > 1}">
                    <p>Page ${posts.number + 1} of ${posts.totalPages}</p>
                </c:if>
            </div>
        </div>

        <div style="margin-top: 20px;">
            <a href="/" class="btn btn-secondary">게시판 목록으로</a>
        </div>

        <%@ include file="../common/footer.jsp" %>