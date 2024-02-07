import ast
from flask import Flask, jsonify, render_template,Response, send_from_directory, request, json
from flask_socketio import SocketIO, emit
from ultralytics import YOLO
import base64
import os
import cv2
import numpy as np
from YOLO_Video import video_detection
import eventlet
import psycopg2
import requests
from PIL import Image
import io

conn = psycopg2.connect(
        host="localhost",
        database="TrafficSigns",
        user="postgres",
        password="582003")
 
model=YOLO(r'D:\PBL4\best.pt')

app = Flask(__name__, static_folder="./templates/static")
app.config["SECRET_KEY"] = "secret!"
socketio = SocketIO(app, async_mode='eventlet')

@app.route("/favicon.ico")
def favicon():
    return send_from_directory(
        os.path.join(app.root_path, "static"),
        "favicon.ico",
        mimetype="image/vnd.microsoft.icon",
    )

#Hàm giải mã base gửi từ client
def base64_to_image(base64_string):
    base64_string = base64_string.strip('\'"')
    base64_string = base64_string.replace('\\n',"")  
    base64_string = base64_string.replace('\\u003d','=')  
    try :
        base64_data = base64_string.split(",")[1]
    except:
        base64_data = base64_string
    image_bytes = base64.b64decode(base64_data)
    image_array = np.frombuffer(image_bytes, dtype=np.uint8)
    image = cv2.imdecode(image_array, cv2.IMREAD_COLOR)
    return image

def image_to_base64(image):
    _, buffer = cv2.imencode(".jpg", image)
    image_bytes = buffer.tobytes()
    base64_data = base64.b64encode(image_bytes).decode("utf-8")
    base64_string = f"data:image/jpeg;base64,{base64_data}"
    return base64_string

def image_to_buffer(image):
    img = np.array(image)
    return img

#Hàm kiểm tra kết nối.
@socketio.on("connect")
def test_connect():
    print("Connected")
    emit("my response", {"data": "Connected"})
    
def getTrafficSignInfo(id):
    cur = conn.cursor()
    try:
        cur.execute("SELECT * FROM trafficsigns WHERE code='" +id+ "'")
        results = cur.fetchall()
        # print(results)
    except:
         results = []
         print("nothing")
    cur.close()
    return results[0]

@socketio.on("image")
def receive_image(image):
    image = base64_to_image(image)
    
    global model
    detection_result,name = video_detection(image,model)
    info = []
    for n in name:
        info.append(getTrafficSignInfo(n))
    print(info)                                                                                                                 
    data = info
    print()
    result, frame_encoded = cv2.imencode(".jpg", detection_result)
    processed_img_data = base64.b64encode(frame_encoded).decode()
    b64_src = "data:image/jpg;base64,"
    processed_img_data = b64_src + processed_img_data
    emit("processed_image", 
         {"image": processed_img_data,
            "info": data
        })
    
@app.route("/all-sign", methods=["GET"])
def all_sign():
    nodejs_api_url = "https://d1k8b3q5-1410.asse.devtunnels.ms/api/sign/all-sign"
    try:
        response = requests.get(nodejs_api_url)
        data_from_nodejs = response.json()
        return data_from_nodejs
    except requests.RequestException as e:
        return f"Error connecting to Node.js API: {str(e)}"
    
def base64_to_buffer(base64_string):
    # Decode the base64 string
    image_data = base64.b64decode(base64_string)
    
    # Chuyển đổi dữ liệu nhị phân thành danh sách số nguyên
    buffer_data = list(image_data)
    return buffer_data

def image_to_json(image):
    image_list = image.tolist()
    json_data = json.dumps(image_list)
    return json_data


@app.route("/detect", methods=["POST"])
def detect():
    try:
        data = request.form
        image = data['image']  
        # print(image)
        image = base64_to_image(image)
        result,name = video_detection(image, model)
        traffic = []
        print(name)
        for n in name:
            print(n)
            traffic.append(getTrafficSignInfo(n))
        print(traffic)
        return jsonify({
            "detect": traffic,
        })
    except Exception as e:
        print(str(e))
        return jsonify({'status': 'error', 'message': str(e)})

@app.route("/")
def index():
    return render_template("index.html")



if __name__ == "__main__":
    socketio.run(app, debug=True, port=5000, host='127.0.0.1')
    
    