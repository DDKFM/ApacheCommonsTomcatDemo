<%@ page import="de.mschaedlich.apache.commons.io.demo.FileSystemListener" %><%--
  Created by IntelliJ IDEA.
  User: MAXIMILIAN
  Date: 17.04.2017
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Apache Commons IO Demo</title>
    <link rel="stylesheet" type="text/css" href="css/font-awesome.min.css"/>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            loadFileList();
            loadLogs();
            var timer;
            timer = window.setInterval(function () {
                loadFileList();
                loadLogs();
            }, 1000);
        })
        function loadFileList() {
            $.ajax({
                url: "file?method=files",
                data: "",
                success: function(data) {
                    $("#fileList").html(data);
                }
            });
        }
        function loadFileContent(filename) {
            $.ajax({
                url: "file?method=filecontent&filename=" + filename,
                data: "",
                success: function(data) {
                    //alert(data);
                    $("#fileContent").html(data);
                }
            });
        }
        function loadLogs() {
            $.ajax({
                url: "file?method=log",
                data: "",
                success: function(data) {
                    $("#logs").html(data);
                }
            });
        }
    </script>
    <style>
        i {
            padding: 3px;
        }
        .white {
            color:white;
        }
        #main {
            width: 100%;
            height: 100%;
            display: block;
        }
        #upperBox {
            width: 100%;
            height: 70%;
            display: block;
        }
        #fileList {
            margin: 0;
        }
        #fileList a {
            text-decoration: none;
            color: black;
            cursor: pointer;
            font-weight: bold;
            vertical-align: top;
        }
        #fileContent {

        }
        #logs {
            width: 100%;
            height: 30%;
            display: block;
        }
    </style>
</head>
<body>
    Link zum Cloud-Ordner(Passwort: cs16-1): <a href="<%= FileSystemListener.getCloudUrl()%>"><%= FileSystemListener.getCloudUrl()%></a>
    <div id="main">
        <table width="100%" style="border: none">
            <tr>
                <td width="50%">
                    <Label><strong>Dateien: </strong></Label>
                </td>
                <td>
                    <Label><strong>Dateinhalt: </strong></Label>
                </td>
            </tr>
            <tr>
                <td width="50%">
                    <div id="fileList"></div>
                </td>
                <td>
                    <pre id="fileContent"></pre>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <Label><strong>Log: </strong></Label>
                    <div id="logs"></div>
                </td>
            </tr>
        </table>
</body>
</html>
