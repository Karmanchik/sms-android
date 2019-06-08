<?php

function loadAppContacts($link) {
  $result = array();
  $query = "SELECT * FROM appmaketcontacts";
  $answer = mysqli_query($link, $query);
  while ($row = mysqli_fetch_array($answer)) {
    $contactJSON = json_encode(array(
      'phone' => $row['phone'],
      'name' => $row['name']
    ));
    $result[] = $contactJSON;
	
  }
  return $result;
}

function loadUserContacts($link) {
    $result = array();
    $query = "SELECT * FROM contactsmaket";
    $answer = mysqli_query($link, $query);
    while ($row = mysqli_fetch_array($answer)) {
      $contactJSON = json_encode(array(
        'phone' => $row['phone'],
        'name' => $row['name']
      ));
      $result[] = $contactJSON;
    }
    return $result;
}
