const net = require('net');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
  <p> WebSocket test page</p>
   
    <textarea id="message" rows="10" cols="50">MyMessage</textarea>
     <textarea id="reply" rows="10" cols="50"></textarea>
     <button id="send" onClick="sendMessage()">Send</button>
    <script>
    function sendMessage() {
    let msg = document.getElementById("message").value;
    ws.send(msg)
    console.log(msg)
   
    
    }
     let reply = document.getElementById("reply");
      let ws = new WebSocket('ws://localhost:3001');
      ws.onmessage = event => {
          reply.innerHTML=event.data;
      }
      ws.onopen = () => ws.send('hello');
    </script>
  </body>
</html>
`;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});
httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});
