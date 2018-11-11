from http.server import BaseHTTPRequestHandler, HTTPServer
import logging
import json
from wit import Wit
from Bot import *
import random
class S(BaseHTTPRequestHandler):
    client = Wit(access_token='JEQC54T6BJZKXPWKSYT5RNXK2P2RAUS3')
    flow = {'intent' : '' , 'types' : '' , 'value' : ''}
    reset = False
    waiting = True
    def do_reset(self):
        S.flow['intent'] = ''
        S.flow['types'] = ''
        S.flow['value'] = ''
    def _set_response(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/json')
        self.end_headers()
    def handle_response_1(self,intent):
        if intent == 'goodbye':
            self._set_response()
            self.wfile.write(json.dumps({'message':'See you later :)'}).encode())
            S.reset = True
        if intent == 'greeting':
            d = ["Greetings to you too!", "Hello there!", "Nice to see you!", "Hello to you too ,friend!"]
            self._set_response()
            self.wfile.write(json.dumps({'message': d[random.randint(0, len(d) - 1)]}).encode())
            S.reset = True
        if intent == 'location':
            self._set_response()
            self.wfile.write(json.dumps({'message' : "Showing you the closest store." , "location" :{'latitude' : 23.5867319 , 'longitude' : 46.7741535}}).encode())
            S.reset = True
        if intent == 'stupid':
            self._set_response()
            self.wfile.write(json.dumps({'message': "Like my creators."}).encode())
            S.reset = True
        if intent == 'do':
            self._set_response()
            self.wfile.write(json.dumps({'message': "Lots of things: try reporting an issue to me or request a renewal of a subscription, help you through the process of a payment or warranty"}).encode())
            S.reset = True
        if intent == 'call':
            self._set_response()
            self.wfile.write(json.dumps({'message': "Calling",'call' : True}).encode())
            S.reset = True
        if intent == 'service':
            self._set_response()
            self.wfile.write(json.dumps({'message': 'Redirecting you :)', 'url' : 'https://www.depanero.ro/#'}).encode())
            S.reset = True
    def handle_response_2(self,intent,type):
        pass
    def handle_response_3(self,intent, type, value):
        if intent == 'payment':
            if type == 'subscription':
                if value == 'internet':
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "Awaiting fingerprint...", "validate": True}).encode())
                    S.reset = True
                elif value == "vodafone":
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "Awaiting fingerprint...", "validate": True}).encode())
                    S.reset = True
            elif type == "phone":
                if value == "samsung":
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "Good choice! I like your style. We can save one for you at the nearest store! Here is where you'll find the store.", "location" :{'latitude' : 23.5867319 , 'longitude' : 46.7741535}}).encode())
                    S.reset = True
                if value == "iphone":
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "Okay great! We can save one for you at the nearest store! Here is where you'll find the store.", "location" :{'latitude' : 23.5867319 , 'longitude' : 46.7741535}}).encode())
                    S.reset = True
        if intent == 'assistance':
            if type == 'phone':
                if value == 'display':
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "Ouch! Want to call an operator or acces our service site?"}).encode())
                    S.reset = True
                if value == 'battery':
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "I hope you didn't get burned! Want to call an operator or acces our service site?"}).encode())
                    S.reset = True

    def get_message(self):
        message = "" + self.path.split("message=", 1)[1]
        message = message.split("%20")
        message = " ".join(message)
        return message
    def get_intent(self, json_msg):
        resp = ["I didn't get that...","Sorry, can you repeat?","What did you mean?"]
        if S.flow['intent'] != '':
            return S.flow['intent']
        if('intent' in json_msg['entities'].keys()):
            if 1 >0.1:
                if check_intent(json_msg['entities']['intent'][0]['value']) != False:
                    return json_msg['entities']['intent'][0]['value']
                else:
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "I don't know how to help with that.", "call": False}).encode())
        elif 'mock' in json_msg['entities'].keys():
                if 1 > 0.1:
                    if check_intent(json_msg['entities']['mock'][0]['value']) != False:
                        return json_msg['entities']['mock'][0]['value']
                    else:
                        self._set_response()
                        self.wfile.write(json.dumps({"message": "I don't know how to help with that.", "call": False}).encode())
        return ''
    def get_type_json(self,json_msg):
        if S.flow['types'] != '':
            return S.flow['types']
        if 'type' in json_msg['entities'].keys():
            if 1 > 0.1:
                if check_type(S.flow['intent'],json_msg['entities']['type'][0]['value']) != False:
                    return json_msg['entities']['type'][0]['value']
                else:
                    self._set_response()
                    self.wfile.write(json.dumps({"message": "Please select one of these options: " + S.flow['intent'] +": "+ ",".join(list(get_intent_repo(S.flow['intent']))), "call": False}).encode())
        self._set_response()
        self.wfile.write(json.dumps({"message": "What type of " + S.flow['intent'] +": "+ ", ".join(list(get_intent_repo(S.flow['intent']))) +"?", "call": False}).encode())
        return ''
    def get_value(self,json_msg):
        if S.flow['value'] != '':
            return S.flow['value']
        if 'values' in json_msg['entities'].keys():
            if 1 > 0.1:
                if check_value(S.flow['intent'],S.flow['types'],json_msg['entities']['values'][0]['value']) != False:
                    return json_msg['entities']['values'][0]['value']
                else:
                    self._set_response()
                    message = get_type_repo(S.flow['intent'], S.flow['types'])
                    message = ",".join(message)
                    print(message)
                    self.wfile.write(json.dumps({"message": "That is not right, select again from list" + ": " + S.flow['types'] + ": " + message,
                                                 "call": False}).encode())
        else:
            self._set_response()
            message = get_type_repo(S.flow['intent'], S.flow['types'])
            message = ",".join(message)
            print(message)
            self.wfile.write(json.dumps({"message": "Please select one"+": " + message, "call": False}).encode())
        return ''
    def handle_chat(self,json_msg):
        no_fields = 0
        if S.flow['intent'] == '':
            S.flow['intent'] = self.get_intent(json_msg)
        if S.flow['intent'] != '':
            no_fields = get_no_fields(S.flow['intent'])
        if(no_fields == 1):
            self.handle_response_1(S.flow['intent'])
        elif no_fields != 0:
            if S.flow['types'] == '':
                type = self.get_type_json(json_msg)
                S.flow['types'] = type
            if(no_fields == 2 and S.flow['types'] != ''):
                self.handle_response_2(S.flow['intent'],S.flow['types'])
            else:
                if S.flow['value'] == '' and S.flow['types'] != '':
                    S.flow['value'] = self.get_value(json_msg)
                if S.flow['value'] != '':
                    self.handle_response_3(S.flow['intent'],S.flow['types'],S.flow['value'])
    def do_GET(self):
        if(S.reset):
            self.do_reset()
            S.reset = False
        user_msg = self.get_message()
        print(user_msg)
        if(user_msg == "reset"):
            self.do_reset()
            self._set_response()
            self.wfile.write(json.dumps({'message':''}).encode())
        else:
            json_msg = S.client.message(user_msg)
            self.handle_chat(json_msg)
            print(S.flow)

    def do_POST(self):
        pass
def run(server_class=HTTPServer, handler_class=S, port=8080):
    logging.basicConfig(level=logging.INFO)
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    logging.info('Starting httpd...\n')
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    logging.info('Stopping httpd...\n')
if __name__ == '__main__':
    from sys import argv
    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()