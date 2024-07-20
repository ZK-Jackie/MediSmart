const express = require('express');
const path = require('path');
const app = express();

app.use(express.static(path.join(__dirname, './dist')));

// 当请求的文件不存在时，返回index.html文件
app.use((req, res) => {
  res.sendFile(path.join(__dirname, './dist', 'index.html'));
});

app.listen(8085);

console.log(`🎉 \n` +
  '  App running at:\n' +
  '  - Local:   http://localhost:8085/ \n' +
  '  - Network: http://10.183.158.241:8085/\n')