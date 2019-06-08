class Sms {
  constructor(name, address, body, date, type) {
    this.name = name+'';
    this.address = address+'';
    this.body = body+'';
    this.date = date + 0;
    this.type = type+'';
  }
}

function getShortFormat(phone) {
  var str = phone + '';
    if (str.length < 10 )
      return str;
    else
      return str.substring(str.length - 10);
}
