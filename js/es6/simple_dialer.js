import axios from 'axios';
import { v4 } from 'uuid';

const initStruct = (unique_id) => {
  return {
    unique_id,
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
};

const startCampaign = (username, password, id) => {
  axios({
    baseURL: 'http://api.dialer.co.il',
    url: '/v2/calls/start_campaign/' + encodeURIComponent(id),
    method: 'POST',
    auth: {
      username,
      password,
    },
    withCredentials: true,
  })
    .then((result) => {
      console.log('success: ', id, result);
    })
    .catch((error) => {
      console.log('error:', id, error);
    });
};

const postRequest = (username, password, id, data)  => {
  axios({
    baseURL: 'http://api.dialer.co.il',
    url: '/v2/calls/simple_dialer',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: JSON.stringify(data),
    auth: {
      username,
      password,
    },
    withCredentials: true,
  })
  .then((result) => {
    console.log('success: ', id, result);
    startCampaign(username, password, id);
  })
  .catch((error) => {
    console.log('error:', id, error);
  });
}

const SimpleDialer = () => {
  const id = v4();
  const struct = initStruct(id);
  postRequest('john', 'doe', id, struct);

};

export default SimpleDialer;