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
                        $('#myModal').modal({});
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

function comment2comment(e) {
    let commentId = e.getAttribute("data-id");
    let content = $("#input-" + commentId).val();
    comment2parent(commentId, 2, content);
}

function getSubComments(e) {
    $(e).toggleClass("active");
    debugger
    let id = e.getAttribute("data-id");
    let subComments = $("#comment-" + id);
    debugger
    if (subComments.children().length == 1) {
        $.getJSON("/comment/" + id, function (data) {
            $.each(data.data.reverse(), function (index, comment) {
                let mediaLeftElement = $("<div/>", {
                    "class": "media-left"
                }).append($("<img/>", {
                    "class": "media-object img-rounded",
                    "src": comment.user.avatarUrl
                }));

                let mediaBodyElement = $("<div/>", {
                    "class": "media-body"
                }).append($("<h5/>", {
                    "class": "media-heading",
                    "html": comment.user.name
                })).append($("<div/>", {
                    "html": comment.content
                })).append($("<div/>", {
                    "class": "menu"
                }).append($("<span/>", {
                    "class": "pull-right",
                    "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                })));

                let mediaElement = $("<div/>", {
                    "class": "media"
                }).append(mediaLeftElement).append(mediaBodyElement);

                let commentElement = $("<div/>", {
                    "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                }).append(mediaElement);

                subComments.prepend(commentElement);
            });
        });
    }
}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    let value = e.getAttribute("data-tag");
    let previous = $("#tag").val();

    if (previous) {
        let index = 0;
        let appear = false; //记录value是否已经作为一个独立的标签出现过
        while (true) {
            index = previous.indexOf(value, index); //value字符串在previous中出现的位置
            if (index == -1) break;
            //判断previous中出现的value是否是另一个标签的一部分
            //即value的前一个和后一个字符都是逗号","或者没有字符时，才说明value是一个独立的标签
            if ((index == 0 || previous.charAt(index - 1) == ",")
                && (index + value.length == previous.length || previous.charAt(index + value.length) == ",")
            ) {
                appear = true;
                break;
            }
            index++; //用于搜索下一个出现位置
        }
        if (!appear) {
            //若value没有作为一个独立的标签出现过
            $("#tag").val(previous + ',' + value);
        }
    } else {
        $("#tag").val(value);
    }
}

function updateUnread() {
    $.ajax({
        url: "/unread",
        success: function (response) {
            let e = $("#notification");
            e.text(response.data);
        }
    });
}

function giveALike(e) {
    let commentId = $(e).attr("data-id");
    $(e).toggleClass("active");
    if (!$(e).hasClass("active")) {
        commentId = -commentId;
    }
    $.ajax({
        url: "/comment/giveALike/" + commentId,
        type: "post",
        success: function () {
            let likeCountSpan = e.getElementsByTagName("span")[1];
            let likeCount = $(likeCountSpan).text();
            if (commentId < 0) {
                likeCount--;
            } else {
                likeCount++;
            }
            $(likeCountSpan).text(likeCount);
        }
    });
}

let interval = setInterval("updateUnread()", 1000 * 5);
