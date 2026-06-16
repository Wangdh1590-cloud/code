const http = require('http');

const PORT = 8088;

const server = http.createServer((req, res) => {
  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  res.end(`
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Hello AI Coding</title>
  <style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
    h1 {
      font-size: 3rem;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
    }
  </style>
</head>
<body>
  <h1>hello ai coding</h1>
</body>
</html>
  `);
});

server.listen(PORT, () => {
  console.log(`Server is running at http://localhost:${PORT}`);
});
