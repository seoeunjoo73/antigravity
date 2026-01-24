<%@ include file="../common/header.jsp" %>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <div class="card">
            <h2>${post == null ? '글쓰기' : '글 수정'}</h2>
            <form
                action="${post == null ? '/board/' : '/board/edit/'}${post == null ? board.id : post.id}${post == null ? '/write' : ''}"
                method="post" enctype="multipart/form-data">

                <div class="form-group">
                    <label>제목</label>
                    <input type="text" name="title" value="${post.title}" required>
                </div>

                <c:if test="${post == null}">
                    <div class="form-group">
                        <label>작성자</label>
                        <input type="text" name="author" required>
                    </div>
                </c:if>

                <div class="form-group">
                    <label>내용</label>
                    <textarea name="content" rows="10" required>${post.content}</textarea>
                </div>

                <div class="form-group">
                    <label>파일 첨부</label>
                    <input type="file" name="file">
                    <c:if test="${not empty post.attachment}">
                        <p style="font-size: 0.8em; color: #666;">현재 파일: ${post.attachment.originalFileName}</p>
                    </c:if>
                </div>

                <div style="margin-top: 20px;">
                    <button type="submit" class="btn btn-primary">저장</button>
                    <a href="javascript:history.back()" class="btn btn-secondary">취소</a>
                </div>
            </form>
        </div>

        <%@ include file="../common/footer.jsp" %>