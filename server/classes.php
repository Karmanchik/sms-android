<?php
  class Sms {
    public $name, $address, $body, $date, $type;
    function __construct($name, $address, $body, $date, $type) {
         $this->name = $name;
         $this->address = $address;
         $this->body = $body;
         $this->date = (int)$date;
         $this->type = $type;
     }
  }

  function getShortFormat($phone='') {
    $str = $phone + '';
    if (strlen($str) < 10)
      return $str;
    else
      return substr($str, strlen($str) - 10);
  }


  class Dialog {
    public $address, $name, $lastBody, $messages, $lastDate;
    function __construct($address, $name) {
      $this->address = $address;
      $this->name = $name;
      $this->lastBody = '';
      $this->messages = array();
      $this->lastDate = 0;
    }
    function addSms($sms) {
      $this->lastDate = $sms->date;
      $this->lastBody = $sms->body;
      $this->messages[] = $sms;
    }

  }

 ?>
