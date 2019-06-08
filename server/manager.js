class Manager {

  constructor(messages) {
    this.dialogs = [];
    for (var sms in messages)
      this.addMessage(sms);
  }

  findDialog(address) {
    console.log("findDialog: "+address);

    for (var index = 0; index < this.dialogs.length; index++)
        if (getShortFormat(this.dialogs[index].address + '') == getShortFormat(address+''))
            return index;
    return -1;
  }

  addMessage(sms) {
    console.log("addmessage: ");
    console.log(sms);
    var index = this.findDialog(sms.address+'');
    if (index == -1) {
        var dialog = new Dialog(sms.address+'', sms.name+'');
        this.dialogs.push(dialog);
        index = this.dialogs.length - 1;
    }
    this.dialogs[index].addSms(sms);
  }

}
