<?php

define('USER_NAME', 'john');
define('PASSWORD', 'doe');
define('BASIC_ADDRESS', 'http://api.dialer.co.il/v2/calls/%s');


function initStruct($unique_id) {
  return array(
    unique_id => $unique_id,
    dial_at => 0,
    from_campaign => false,
    leg_a => '97237173333',
    cid_a => '037173333',
    dtmf_a => 'W1',
    tts => true,
    sound => 'Hello world',
    gender => 'female',
    answer_delay => 2,
    ring_timeout => 15,
    max_retries => 3,
    retry_wait_time => 120,

    # do not call to a destination after a message was provided
    # leg_b => '',
    # cid_b => '',
    # dtmf_b => '',

  );
}

function postRequest($username, $password, $action, $data=null) {
   $uri = sprintf(BASIC_ADDRESS, urlencode($action));
}

