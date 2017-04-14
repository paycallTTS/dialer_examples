

function initStruct(id) {
  return {
    unique_id: id,
    dial_at: 0,
    from_campaign: false,
    leg_a: '97237173333',
    cid_a: '037173333',
    dtmf_a: 'W1',
    tts: true,
    sound: 'Hello world',
    gender: 'female',
    answer_delay: 2,
    ring_timeout: 15,
    max_retries: 3,
    retry_wait_time: 120,

    /*
     do not call to a destination after a message was provided
     leg_b: '',
     cid_b: '',
     dtmf_b: '',
    */
  }
}

function initRequest(username, password, id) {
  if (!jQuery) {
    return;
  }

  $.ajax({
    url: 'http://api.dialer.co.il/v2/calls/start_campaign/' + encodeURIComponent(id),
    method: 'POST',
    username: username,
    password: password,
    success: function (retData, statusText) {
      console.log('success', retData, statusText);
    },
    error: function (xhr, textStatus, errorThrown) {
      console.log('error', textStatus, errorThrown);
    }
  });
}

function postRequest(username, password, id, data) {
  if (! jQuery) {
    return;
  }

  $.ajax({
    url: 'http://api.dialer.co.il/v2/calls/simple_dialer',
    method: 'POST',
    data: JSON.stringify(data),
    username: username,
    password: password,
    contentType: 'application/json',
    success: function(retData,statusText) {
      console.log('success ', id, retData, statusText);
      initRequest(username, password, id);
    },
    error: function(xhr, textStatus, errorThrown) {
      console.log('error', id, textStatus, errorThrown, xhr);
    }
  });
}

$(function(){
  var min = 1;       // 1
  var max = 2 ** 32; // 4294967296

  var id = Math.floor(Math.random() * (max - min + 1)) + min;
  console.log('id', id);
  postRequest('john', 'doe', id, initStruct(id));
});
