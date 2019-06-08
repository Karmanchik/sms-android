function createWithClass(tag, className) {
  var result = document.createElement(tag);
  result.setAttribute('class', className);
  return result;
}

function addNewSms(list, sms) {
  var exist = false;
  console.log(list);
  for (var i = 0; i< list.length; i++) {
    console.log(list[i]['address'] +"==="+sms['address']);
    if (list[i]['address'] == sms['address']) {
      list[i].addSms(sms);
      exist = true;
    }
  }
  if (exist == false) {
      var tmp = new Dialog(sms['address'], sms['name']);
      tmp.addSms(sms);
      list.push(tmp);
  }
  return list;
}

function createSms(index, i) {
    var sms = dialogsList[index]['messages'][i];

    var startPosInBox = '<div class="LBrqSb KXKOS ReMHf Ksikcc"><div class="DKMVFf"><div class="tHxeDb"><div class="lXNy8e oLerdd zshGI INJlr" style="--message-color:#FEEFC3; --message-color-dark:#eadcb3">';
    var endPosInBox = '</div></div></div></div>';

    var startPosOutBox = '<div class="LBrqSb KXKOS Y58cPb OCVEnc"><div class="DKMVFf"><div class="tHxeDb"><div class="YxoJ7  oLerdd zshGI INJlr" style="--message-color:#fff; --message-color-dark:#202124">';
    var endPosOutBox = '</div></div></div></div>';

    var container = document.createElement('section');
    if (sms['type'] == 'INBOX') {
      container.setAttribute('class', 'boPWvd r59vlc');
      container.innerHTML = startPosInBox + sms['body'] + endPosInBox;
    }
    else {
      container.setAttribute('class', 'boPWvd RszMOe');
      container.innerHTML = startPosOutBox + sms['body'] + endPosOutBox;
    }

    return container;
}

function updMessages(index) {
  var chatbox = document.getElementById('chatbox');

  var messages = chatbox.getElementsByTagName('section');
  while (chatbox.firstChild)
    chatbox.removeChild(chatbox.firstChild);

  //прогрузить в него новое
  var msgs = dialogsList[index]['messages'];
  for (var i = 0; i < msgs.length; i++) {
    chatbox.appendChild(createSms(index, i));
  }
}

function updDialogs(address) {

  currentAddress = address;

  for (var index in dialogsList) {
    try {
      var tmpAddress = dialogsList[index]['address'];
      if (tmpAddress == address) {
        document.getElementById(address).setAttribute('class', 'Fmgnmf braVhe');
        document.getElementById('chatTitle').innerHTML = dialogsList[index]['name'];
        updMessages(index);
      }
      else
        document.getElementById(tmpAddress).setAttribute('class', 'Fmgnmf');
    }
    catch(err) {}
  }

}


function generDialogItem(name, lastBody, address) {
  var item = createWithClass('content', 'N9wOvf fICMMe');
  item.onclick = function (event) {
    updDialogs(address);
  };

  var item1 = createWithClass('div', 'Cgh4Db MbhUzd');

  var item2 = createWithClass('div', 'clmEye');

  var item21 = createWithClass('div', 'Fmgnmf');
  item21.setAttribute('id', address);
  item21.setAttribute('name', 'theme');

  var item211 = createWithClass('div', 'CaAkD RntNLb');

  var item2111 = createWithClass('img', 'qkdzle MPRaLc xqCI7b');
  item2111.setAttribute('alt', '');
  item2111.setAttribute('src', 'images/face.png');

  var item212 = createWithClass('div', 'EnEC8');

  var item2121 = createWithClass('div', 'tpEAA');

  var item21211 = createWithClass('div', 'ReFmyd');
  item21211.setAttribute('style', 'color: white;');
  item21211.innerHTML = name;

  var item2122 = createWithClass('div', 'jI8Ljd');

  var item21221 = createWithClass('div', 'TAyRfc');
  item21221.setAttribute('style', 'color: gray;');
  item21221.innerHTML = lastBody;

  item2121.appendChild(item21211);
  item2122.appendChild(item21221);
  item212.appendChild(item2121);
  item212.appendChild(item2122);
  item211.appendChild(item2111);
  item21.appendChild(item211);
  item21.appendChild(item212);
  item2.appendChild(item21);
  item.appendChild(item1);
  item.appendChild(item2);

  return item;
}

function sendSms() {
 var body = document.getElementById("textField").innerText;
 document.getElementById("textField").innerText = '';
 document.getElementsByTagName('iframe')[0].contentWindow.location.href = "dialogs.php?"+
                  "action=sendSms&" +
                  "token=" + token +
                  "&body=" + body +
                  "&name=" + document.getElementById('chatTitle').innerHTML +
                  "&address="+currentAddress;
}
