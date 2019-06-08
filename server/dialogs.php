<?php
  require_once 'connect.php';

  $link = mysqli_connect($host, $user, $password, $database);
  $metod = $_GET['action'];

  if ($metod == "isConnected") {
    $token = $_GET['token'];
    $query = "SELECT * FROM token WHERE token='".$token."' AND uses='true'";
    $answer = mysqli_query($link, $query);
    while ($row = mysqli_fetch_array($answer)) {
      echo '<script>var result = "true";</script>';
    }
  }
  //anroid-client подключается по токену из QR-кода::возвращает есть ли такой токен
  if ($metod == "connectByToken") {
      $token = $_GET['token'];
      $now = getdate()['0'];
      //delete old tokens
      $minDate = $now - 86400;
      $query = "DELETE FROM token WHERE date<".$minDate;
      mysqli_query($link, $query);
      //connect
      $query = "UPDATE token SET uses='true' WHERE token='".$token."'";
      mysqli_query($link, $query);
  }
  //android-client загружает все диалоги на сервер
  if ($metod == "loadDialogs") {
    $token = $_GET['token'];
    $name = $_GET['address'];
    if (isset($_GET['name']))
      $name = $_GET['name'];
    $query = "INSERT INTO tempmessage(date, body, type, address, name, token) VALUES(".$_GET['date'].",'".$_GET['body']."','".$_GET['type']."','".$_GET['address']."','".$name."','".$token."')";
    mysqli_query($link, $query);
  }
  //web-client загружает с сервера все диалоги
  if ($metod == "getDialogs") {
    $token = $_GET['token'];
    $query = "SELECT * FROM tempmessage WHERE token='".$token."'";
    $result = array();
    $answer = mysqli_query($link, $query);
    while ($row = mysqli_fetch_array($answer))  {
      $result[] = json_encode(array(
        'date' => $row['date'],
        'body' => $row['body'],
        'type' => 'inbox',
        'address' => $row['address'],
        'name' => $row['name']
      ));
    }
    echo json_encode($result);
  }
  //web-client отправляет смс для отправки с android-client'a
  if ($metod == "sendSms") {
    $now = getdate()['0'];
    $token = $_GET['token'];
    $body = $_GET['body'];
    $address = $_GET['address'];
    $name = $address;
    if (isset($_GET['name']))
      $name = $_GET['name'];
    $query = "INSERT INTO tempmessage(date, body, type, address, name, token) VALUES(".$now.",'".$body."','draft','".$address."','".$name."','".$token."')";
    mysqli_query($link, $query);
  }
  //web-client проверяет есть ли новые входящие
  if ($metod == "getNewSms") {
    echo '<script src="js/sms.js"></script>
    <script src="js/dialog.js"></script>
    <script src="js/manager.js"></script>
    <script src="js/ui.js"></script>';
    echo '<script>
            var array = [];
            var tempSms = new Sms("", "", "", 0, "");
          </script>';
    $token = $_GET['token'];
    $query = "SELECT * FROM tempmessage WHERE token='".$token."' AND type='inbox'";
    $answer = mysqli_query($link, $query);
    while ($row = mysqli_fetch_array($answer)) {
      echo '<script> tempSms = new Sms(';
      echo '"'.$row["name"].'",';
      echo '"'.$row["address"].'",';
      echo '"'.$row["body"].'",';
      echo ''.$row["date"].',';
      echo '"'.$row["type"].'"';
      echo ');';
      echo 'array.push(tempSms); </script>';
    }
    echo '<script>';
    $query = "DELETE FROM tempmessage WHERE token='".$token."' AND type='inbox'";
    mysqli_query($link, $query);
    echo 'parent.document.getElementById("dialogslist").innerHTML = "";';
    echo 'window.parent.createList();';
    echo '</script>';
  }
  //android-client проверяет наличие смс для отправки
  if ($metod == "getDraftToSend") {
    $token = $_GET['token'];
    $result = array();
    $query = "SELECT * FROM tempmessage WHERE token='".$token."' AND type='draft'";
    $answer = mysqli_query($link, $query);
    while ($row = mysqli_fetch_array($answer)) {
      $result[] = json_encode(array(
        'body' => $row['body'],
        'address' => $row['address']
      ));
    }
    $query = "DELETE FROM tempmessage WHERE token='".$token."' AND type='draft'";
    mysqli_query($link, $query);
    echo utf8_encode(json_encode($result));
  }
  //android-client отправляет новые входящие сообщения
  if ($metod == "loadNewSms") {
    $date = $_GET['time'];
    $token = $_GET['token'];
    $body = $_GET['body'];
    $address = $_GET['address'];
    $name = $address;
    if (isset($_GET['name']))
      $name = $_GET['name'];
    $query = "INSERT INTO tempmessage(date, body, type, address, name, token) VALUES (".$date.",'".$body."','inbox','".$address."','".$name."','".$token."')";
    mysqli_query($link, $query);
  }

 ?>
