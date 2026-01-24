<%@ include file="../common/header.jsp" %>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <div class="card">
            <div style="border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">
                <h3>${post.title}</h3>
                <div style="color: #666; font-size: 0.9em; display: flex; gap: 20px;">
                    <span>작성자: ${post.author}</span>
                    <span>작성일: ${post.createdAt}</span>
                </div>
            </div>

            <div style="min-height: 200px; white-space: pre-wrap;">${post.content}</div>

            <c:if test="${not empty post.attachment}">
                <div style="margin-top: 30px; padding: 10px; background: #f9f9f9; border-radius: 4px;">
                    <strong>첨부파일:</strong>
                    <a href="${post.attachment.filePath}" target="_blank">${post.attachment.originalFileName}</a>
                </div>
            </c:if>
        </div>

        <div style="margin-top: 20px; display: flex; gap: 10px;">
            <a href="/board/${post.board.id}" class="btn btn-secondary">목록</a>
            <a href="/board/edit/${post.id}" class="btn btn-primary">수정</a>
            <form action="/board/delete/${post.id}" method="post" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                <button type="submit" class="btn btn-danger">삭제</button>
            </form>
        </div>

        <%@ include file="../common/footer.jsp" %>