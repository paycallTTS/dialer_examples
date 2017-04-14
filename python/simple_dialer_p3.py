#!/usr/bin/env python3
'''
The following API can be found at:
    https://github.com/paycallTTS/api-documentation
Simple Dialer API -> Page 24
'''

# The following code is using Python 3.6.0 syntax

import requests
from requests.auth import HTTPBasicAuth
import json
import urllib
from uuid import uuid4
import sys

USER_NAME = 'john'
PASSWORD = 'doe'
BASIC_ADDRESS = 'http://api.dialer.co.il/v2/calls/%s'


def init_struct(unique_id):
    return {
        'unique_id': unique_id,
        'dial_at': 0,
        'from_campaign': False,
        'leg_a': '97237173333',
        'cid_a': '037173333',
        'dtmf_a': 'W1',
        'tts': True,
        'sound': 'Hello world',
        'gender': 'female',
        'answer_delay': 2,
        'ring_timeout': 15,
        'max_retries': 3,
        'retry_wait_time': 120,
    }


def post_request(username, password, action, data=None):
    if data is None:
        connection = requests.post(BASIC_ADDRESS % action,
                                   auth=HTTPBasicAuth(username, password),
                                   data=json.dumps(data)
                                   )
    else:
        connection = requests.post(BASIC_ADDRESS % action,
                                   auth=HTTPBasicAuth(username, password))

    if connection.status_code == 200:
        return True

    return (connection.status_code, connection.content)


def start_campaign(username, password, id):
    return post_request(username, password,
                        'start_campaign/%s' % urllib.parse.quote_plus(id))


id = str(uuid4())
struct = init_struct(id)

result = post_request(USER_NAME, PASSWORD, 'simple_dialer', struct)

if result is not True:
    print("Unable to initiate simple campaign: %s\n%s\n" % (
        result[0], result[1]), file=sys.stderr)
    sys.exit(1)


result = start_campaign(USER_NAME, PASSWORD, id)
if result is not True:
    print("Unable to initiate campaign: %s\n%s\n" % (
        result[0], result[1]), file=sys.stderr)
    sys.exit(2)

print('Campaign #%s was started successfully.' % id)
