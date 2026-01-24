<%@ include file="common/header.jsp" %>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <div class="card">
            <h2>새 게시판 만들기</h2>
            <form action="/board/create" method="post">
                <div class="form-group">
                    <label>게시판 이름</label>
                    <input type="text" name="name" required placeholder="예: 공지사항, 자유게시판">
                </div>
                <div class="form-group">
                    <label>설명</label>
                    <input type="text" name="description" placeholder="게시판에 대한 간단한 설명">
                </div>
                <button type="submit" class="btn btn-primary">게시판 생성</button>
            </form>
        </div>

        <h2>게시판 목록</h2>
        <div class="board-grid">
            <c:forEach var="board" items="${boards}">
                <div class="board-card">
                    <h3><a href="/board/${board.id}">${board.name}</a></h3>
                    <p>${board.description}</p>
                    <div style="margin-top: 10px; display: flex; justify-content: space-between;">
                        <a href="/board/${board.id}" class="btn btn-secondary">입장</a>
                        <form action="/board/delete" method="post" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                            <input type="hidden" name="id" value="${board.id}">
                            <button type="submit" class="btn btn-danger">삭제</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty boards}">
                <p>생성된 게시판이 없습니다. 상단에서 게시판을 만들어보세요!</p>
            </c:if>
        </div>

        <%@ include file="common/footer.jsp" %>