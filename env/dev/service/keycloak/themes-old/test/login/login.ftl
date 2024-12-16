<!-- login.ftl -->
<!DOCTYPE html>
<html>
<head>
    <title>Custom Login</title>
    <link rel="stylesheet" type="text/css" href="${url.resourcesPath}/css/styles.css">
</head>
<body>
    <div class="login-container">
        <img src="${url.resourcesPath}/img/logo.png" alt="Logo">
        <h1>Welcome to Custom Keycloak Theme</h1>
        <form id="kc-form-login" method="post" action="${url.loginAction}">
            <label for="username">Username</label>
            <input type="text" id="username" name="username">
            <label for="password">Password</label>
            <input type="password" id="password" name="password">
            <button type="submit">Login</button>
        </form>
    </div>
    <script src="${url.resourcesPath}/js/script.js"></script>
</body>
</html>
