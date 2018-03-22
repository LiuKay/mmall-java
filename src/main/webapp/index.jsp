<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<html>
<body>

<h3>springMVC 上传测试</h3>

文件上传测试
<form action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <button type="submit" value="文件上传">文件上传</button>
</form>

富文本上传测试
<form action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <button type="submit" value="富文本上传">富文本上传</button>
</form>
</body>
</html>
