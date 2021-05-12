
<!DOCTYPE html>
<?php
  $price = "price!!!";
  $coin = $_GET['coin']
?>

<html>
<head>
	<meta charset="utf-8" />
	<title>WebSocket 테스트</title>
	<script language="javascript" type="text/javascript">

		var wsUri = "wss://api.upbit.com/websocket/v1";
		var output;

		function init()
		{
			output = document.getElementById("output");
			testWebSocket();
		}
		function testWebSocket()
		{
			websocket = new WebSocket(wsUri);
			websocket.binaryType = 'arraybuffer';
			//websocket.binaryType = 'Blob';
			//websocket.binaryType = 'String';
			websocket.onopen = function(evt) { onOpen(evt) ;};
			//websocket.onclose = function(evt) { onClose(evt) };
			websocket.onmessage = function(evt) { onMessage(evt) };
			websocket.onerror = function(evt) { onError(evt) };
		}

		function onOpen(evt)
		{
			writeToScreen("연결완료");

      var msg = [
              {
                "ticket": "TEST",
              },
              {
                "type": "ticker",
                "codes": ["<?php echo $coin?>"]
              }
    ];

			msg = JSON.stringify(msg);
			doSend(msg);
		}

		function onClose(evt)
		{
			writeToScreen("연결해제");
		}

		function onMessage(evt)
		{
			//console.log(evt.data);
			var enc = new TextDecoder("utf-8");

			var arr = new Uint8Array(evt.data);

			console.log(enc.decode(arr));

      writeToScreen('<span style="color: blue;">수신: ' +JSON.parse(enc.decode(arr)).code+ " = "+ JSON.parse(enc.decode(arr)).trade_price+'</span>')

//			websocket.close();

		}

		function onError(evt)
		{
			writeToScreen('<span style="color: red;">에러:</span> ' + evt.data);
		}

		function doSend(message)
		{
			writeToScreen("발신: " + message);
			websocket.send(message);
		}

		function writeToScreen(message)
		{
			var pre = document.createElement("p");
			pre.style.wordWrap = "break-word";
			pre.innerHTML = message;
			output.appendChild(pre);

		}
		window.addEventListener("load", init, false);

	</script>
</head>

<body>

	<h2>WebSocket Test</h2>

	<div id="output"></div>

</body>

</html>
