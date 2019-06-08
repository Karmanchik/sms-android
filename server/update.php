<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<?php
  require_once 'connect.php';
  require_once 'updateMetods.php';

  $link = mysqli_connect($host, $user, $password, $database);
  $metod = $_GET['action'];
  $result = array();

  if($metod == "loadAppContacts") {
    $result = loadAppContacts($link);
  }
  if($metod == "loadUserContacts") {
    $result = loadUserContacts($link);
  }

  echo utf8_encode(json_encode($result));
 ?>
