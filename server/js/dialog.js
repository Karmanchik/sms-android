class Dialog {
  constructor(address, name) {
    this.address = address + '';
    this.name = name + '';
    this.lastBody = '';
    this.messages = [];
    this.lastDate = 0;
  }


  addSms(sms) {
    if (sms.date+0 > this.lastDate+0) {
      this.lastDate = sms.date;
      this.lastBody = sms.body;
    }
    this.messages.push(sms);
  }

  sort() {
    for (var i = 0; i < this.messages.length; i++)
      for (var j = i + 1; j < this.messages.length; j++)
        if (this.messages[i] > this.messages[j]) {
          var temp = this.messages[i];
          this.messages[i] = this.messages[j];
          this.messages[j] = temp;
        }
    this.body = this.messages[this.messages.length - 1]['body'];
  }

  isNotOneDay(first, second) {
      var one1 = new Date(first);
      var two1 = new Date(second);

      if (one1.getFullYear() == two1.getFullYear())
        if (one1.getMonth() == two1.getMonth())
          if (one1.getDate() == two1.getDate())
            return false;
      return true;
  }
}
