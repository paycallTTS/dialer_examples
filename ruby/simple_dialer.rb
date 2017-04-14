#!/usr/bin/env ruby

require 'json'
require 'securerandom'
require 'net/http'
require 'uri'

USER_NAME = 'john'
PASSWORD  = 'doe'
BASIC_ADDRESS = 'http://api.dialer.co.il/v2/calls/%s'

def init_struct(unique_id)
  # The following API can be found at: https://github.com/paycallTTS/api-documentation
  # Simple Dialer API -> Page 24
  {
    unique_id: unique_id,
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

    # do not call to a destination after a message was provided
    # leg_b: '',
    # cid_b: '',
    # dtmf_b: '',
  }
end

def post_request(username, password, action, data=nil)
  uri = URI(BASIC_ADDRESS % action)
  http = Net::HTTP::new(uri.host, uri.port)
  req = Net::HTTP::Post.new(uri)
  req.basic_auth username, password

  unless data.nil?
    req['Content-Type'] = 'application/json'
    req.body = data.to_json
  end

  answer = http.request(req)

  return true if answer.code == 200
  # return for both code and body on non 200 ok request
  [answer.code, answer.body]
end

def start_campaign(username, password, id)
  post_request(username, password, "start_campaign/#{URI.escape(id)}")
end

id = SecureRandom.uuid
struct = init_struct(id)

result = post_request(USER_NAME, PASSWORD, 'simple_dialer', struct)

unless result == true
  $stderr.puts("Unable to initiate simple campaign: #{result[0]}\n#{result[1]}")
  exit(1)
end

result = start_campaign(USER_NAME, PASSWORD, id)
unless result == true
  $stderr.puts("Unable to initiate campaign: #{result[0]}\n#{result[1]}")
  exit(2)
end

puts "Campaign ##{id} was started successfully"
