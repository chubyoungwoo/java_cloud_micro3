#!/usr/bin/env bash
curl --header "Content-Type: application/json" \
   --request POST \
   --data '{"productId":9, "productName":"9_name","productinfo":"9_productInfo","recommendList":[{"recommendId":91,"author":"Author91","content":"Content91"}],"reviewList":[{"reviewId":1,"author":"Author1","subject":"Subject1","content":"Content1"}]}' \
http://123.213.1.211:8080/composite

curl http://123.213.1.211:8080/composite/9 | jq

curl -X "DELETE" http://123.213.1.211:8080/composite/9