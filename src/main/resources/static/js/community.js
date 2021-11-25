/**
 * 提交回复
 */
function comment2parent(parentId, type, content) {
    if (!content) {
        alert("回复不能为空");
        return;
    }
    // 发送ajax请求
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/comment",
        data: JSON.stringify({
            "parentId": parentId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            // 请求成功
            if (response.code == 200) {
                // 页面刷新
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    let isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=1accb29542174c46cbc9&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment2question() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    comment2parent(questionId, 1, content);
}

function comment2comment() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    comment2parent(questionId, 1, content);
}