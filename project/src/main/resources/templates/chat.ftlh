<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>List of all authors</title>
    <style type="text/css">
        body {
            padding: 50px;
        }

        .errors {
            color: red;
        }
    </style>
    <script src="/webjars/jquery/3.6.4/jquery.min.js"></script>
</head>
<body>
<h3>Welkome to chat</h3>

<div id="error" class="errors"></div>

<div>
    <label>
        <textarea id="chat-textarea"></textarea>
    </label>
</div>
<button id="send-button" onclick="sendMessage()">Send</button>

<script>
    $(function () {
        <#--$.get('/api/v1/authors').done(function (authors) {-->
        <#--    authors.forEach(function (author) {-->
        <#--        $('tbody').append(`-->
        <#--            <tr id="author_tr_${author.id}">-->
        <#--                <td>${author.id}</td>-->
        <#--                <td>${author.fullName}</td>-->
        <#--                <td>-->
        <#--                    <a href="/authors/edit/${author.id}">Edit</a>-->
        <#--                    <a href="#" onclick="deleteAuthor(${author.id})">Delete</a>-->
        <#--                </td>-->
        <#--            </tr>-->
        <#--        `)-->
        <#--    });-->
        <#--})-->
    });

    function sendMessage() {
        let textarea = $("#chat-textarea");
        let val = textarea.val();
        textarea.val("");
        alert(val);
    }
</script>
</body>
</html>
