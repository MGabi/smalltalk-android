import json
from Server import *
entities={
          "payment":
              {"bill":["water","electricity","heat"],
               "subscription":["vodafone","internet"],
               "phone": ["iphone", "samsung"]},
            "assistance":{'phone':['display','battery']},

          "greeting":'',
            "call":'',
            "service":"",
          "location":'',
            "Goodbye":'',
          "test":{"testV":""}}
def get_no_fields(intent):
     if entities[intent] == '':
         return 1
     else:
         lst = list(entities[intent].keys())
         if entities[intent][lst[0]] == '':
             return 2
         else:
             return 3
def check_intent(intent):
    if intent in entities.keys():
        return True
    else:
        return False
def check_type(intent,types):
    if types in entities[intent].keys():
        return True
    else:
        return False
def check_value(intent,types,value):
    if value in get_type_repo(intent,types):
        return True
    else:
        return False
def get_intent_repo(intent):
    if intent in entities.keys():
        return entities[intent]
    else:
        return ''
def get_type_repo(intent,types):
    if types in entities[intent].keys():
        return entities[intent][types]
    else:
        return ''
def get_value_repo(intent,types,value):
    if value in check_type(intent,types):
        return entities[intent][types]
    else:
        return ''
print(type(check_type('payment' , 'subscription')))
print(check_intent('location'))