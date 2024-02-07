from ultralytics import YOLO
import cv2
import math
import numpy as np


def video_detection(img,model):
    classNames = ['DP.135', 'P.102', 'P.103a', 'P.103b', 'P.103c', 'P.104', 'P.106a', 'P.106b', 'P.107a', 'P.112', 'P.115', 'P.117', 'P.123a', 'P.123b', 'P.124a', 'P.124b', 'P.124c', 'P.125', 'P.127', 'P.128', 'P.130', 'P.131a', 'P.137', 'P.245a', 'R.301c', 'R.301d', 'R.301e', 'R.302a', 'R.302b', 'R.303', 'R.407a', 'R.409', 'R.425', 'R.434', 'S.509a', 'W.201a', 'W.201b', 'W.202a', 'W.202b', 'W.203b', 'W.203c', 'W.205a', 'W.205b', 'W.205d', 'W.207a', 'W.207b', 'W.207c', 'W.208', 'W.209', 'W.210', 'W.219', 'W.221b', 'W.224', 'W.225', 'W.227', 'W.233', 'W.235', 'W.245a']
    print("4")
    name = ""
    while True:
        results=model(img,stream=True)
        name = []
        for r in results:
            boxes=r.boxes
            for box in boxes:
                conf=math.ceil((box.conf[0]*100))/100
                if(conf > 0.5):
                    x1,y1,x2,y2=box.xyxy[0]
                    x1,y1,x2,y2=int(x1), int(y1), int(x2), int(y2)
                    print(x1,y1,x2,y2)
                    cv2.rectangle(img, (x1,y1), (x2,y2), (255,0,255),3) 
                    cls=int(box.cls[0])
                    class_name=classNames[cls]
                    label=f'{class_name}{conf}'
                    t_size = cv2.getTextSize(label, 0, fontScale=1, thickness=2)[0]
                    print(t_size)
                    c2 = x1 + t_size[0], y1 - t_size[1] - 3
                    cv2.rectangle(img, (x1,y1), c2, [255,0,255], -1, cv2.LINE_AA)  # filled
                    cv2.putText(img, label, (x1,y1-2),0, 1,[255,255,255], thickness=1,lineType=cv2.LINE_AA)
                    print("video_detection: " + r.names[int(box.cls[0])])
                    # if r.names[int(box.cls[0])] not in name:
                    name.append(r.names[int(box.cls[0])])
        return img,name
        
    



