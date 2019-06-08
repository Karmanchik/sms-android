<?php
  require_once 'connect.php';
  $link = mysqli_connect($host, $user, $password, $database);

  if($_GET['action'] == "addcontact") {
    $contact = json_decode($_GET['data']);
    $dat = getdate()['0'];
    $exist = "false";
    $phone = $contact->{'phone'};
    $q = "SELECT * FROM contactsmaket WHERE phone='".$phone."'";
    $answer = mysqli_query($link, $query);
    while ($row = mysqli_fetch_array($answer)) {
	$exist = "true";
	break;
    }
    if ($exist == "false") {
	$query = "INSERT INTO contactsmaket(phone, name, dateLoad) VALUES ('".$contact->{'phone'}."','".$contact->{'name'}."',".$dat.")";
	mysqli_query($link, $query);
    }
  }
