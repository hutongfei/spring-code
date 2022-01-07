[#--[#ftl]--]
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript" src="${base}/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery.validate.js"></script>
    <style>
        .inputClass {
            color: red;
        }
    </style>
</head>
<body>
<br>
<div id="div2">
    1 <input type="checkbox" value="1">
    2 <input type="checkbox" value="2">
    3 <input type="checkbox" value="3">
    4 <input type="checkbox" value="4">
</div>

<div id="div3">
    <h5>1</h5>
    <h5>2</h5>
    <h5>3</h5>
    <h5>4</h5>
</div>
id:
<input type="text" value="${id}"/>
<input type="button" value="点击" id="btnn">
${id}
[#--<input type="button" value="获取" id="btn">--]
[#--<br>--]

<div id="div4">
    <input id="addBtn" type="button" value="增加"/>
    <table style="border: white solid 1px" width="500px" cellspacing="1">
        <th>
            <tr>
                <td>ID</td>
                <td>NAME</td>
                <td>OPERATE</td>
            </tr>
        </th>
        <tbody id="tby">
        <tr>
            <td>ID</td>
            <td>NAME</td>
            <td><input type="button" value="remove"/></td>
        </tr>
        </tbody>
    </table>

    <div id="d1">
        aaa
        <div id="d2">
            bbb
            <div id="d3">
                <input type="text" value="111" class="inputClass">
                <input type="text" value="222" disabled="disabled">
                <input type="text" value="333" class="inputClass" disabled="disabled">
                <input type="checkbox">
                <input type="radio">
                <input type="button" value="test" id="test">

            </div>
        </div>
    </div>


    <h1><input type="button" id="testBtn" value="测试一下" /></h1>
</div>


[#if b1 || !b2 && !b3]
<h1>aaaaa</h1>
[/#if]

b1 false
b2 false
b3 false

show   (b1 || !b2) && !b3

 show  b1 || (!b2 && !b3)


<script type="text/template" id="addTpl">
    <tr>
        <td>ID</td>
        <td>NAME</td>
        <td><input type="button" value="remove"/></td>
    </tr>
</script>


<script type="text/template" id="demo1">
    <div>
        <h5>${user.id}</h5>
        <h5>${user.userName}</h5>
        <h5>${user.price}</h5>
        <h5>${user.flag?string("true","false")}</h5>
        <h5>${user.createDate?string("yyyy-MM-dd HH:mm:ss")!}</h5>

    </div>
</script>

<script type="text/javascript">
    $(function () {

        $("#btn").click(function () {
            let html = document.querySelector("#demo1").innerHTML;
            $("#div1").append(html)
        });
        $("#testBtn").click(function () {
            debugger
            var mm = '${flag}';

            if (mm) {
                alert(mm)
            }

            [#--if ('${flag}' === true) {--]
            [#--    alert('${flag}')--]
            [#--}--]
            [#--if ('${flag1}' && '${flag1}' === false) {--]
            [#--    alert('${flag1}')--]
            [#--}--]
        });

        $("#div3").find("h5").css("display", "none");

        $("#div2 input:checkbox").change(function () {
            if ($(this).prop('checked') === true) {
                var value = $(this).prop('value');
                value = value - 1;
                $("#div3").find("h5").eq(value).css("display", "")

            } else {
                var value = $(this).prop('value');
                value = value - 1;
                console.log($("#div3").find("h5").eq(value).html())
                $("#div3").find("h5").eq(value).css("display", "none")
            }
        });

        var i = 0;
        $("#addBtn").click(function () {
            // var innerHTML = document.querySelector("#addTpl").innerHTML;

            i++;
            $("#tby").append('<tr><td>' + i + '</td><td>NAME</td><td><input type="button" value="remove" /></td></tr>');
            $("#tby tr:odd").css("color", "red");
        });

        // $("#tby").find("input:button").click(function () {
        //     debugger
        //     let html1 = $(this).html();
        //     console.log(html1)
        // });

        $("#tby input[type='button']").click(function () {
            debugger
        });


        // $("#tby").on("click","input:button", function () {
        //     debugger
        //    $(this).parents("tr").eq(0).remove();
        // });

        $("#test").click(function () {
            $("#d3 input.inputClass:not(:disabled)").each(function () {
                debugger
                let val = $(this).val();
                console.log(val)
            });
        });


        $("#btnn").click(function () {
            location.href = "/page/index"
        });
    });
</script>

</body>
</html>