#!/usr/bin/python3

import sys
import numpy as np
import os
# import matplotlib.pyplot as plt
import imageio
import corner_detection
import perspective_transform
import text_detection
import cv2
import re
# import random

if(len(sys.argv) > 1):
    
    # img_data = sys.argv[1]

    # img = img_data.decode('base64')
    img = imageio.imread(sys.argv[1])
    # print("Detecting card corners....")
    # img1_corners = corner_detection.CornerDetector(img).corner_detector()[0]
    # plt.figure(figsize=(20, 20))
    # plt.imshow(img1_corners)
    # plt.show()

    # print("Repairing card perspective....")
    # corner_points = corner_detection.CornerDetector(img).find_corners4().astype(
    # np.float32)
    # corner_points[:, [0, 1]] = corner_points[:, [1, 0]]
    # img1_t = perspective_transform.PerspectiveTransform(
    # img, corner_points).four_point_transform()
    # plt.figure(figsize=(20, 20))
    # plt.imshow(img1_t)
    # plt.show()

    # print("Detecting text in card...")
    # img1_t_cv = cv2.cvtColor(img1_t, cv2.COLOR_RGB2BGR)
    img1_t_cv = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

    strs, bboxes1, _ = text_detection.TextDetector(img1_t_cv,(30, 10)).recognize_text()
    
    for box in bboxes1:
        x, y, w, h = box
        cv2.rectangle(img1_t_cv, (x, y), (x + w, y + h), (0, 0, 255), 3, 8, 0)

    # plt.figure(figsize=(10, 10))
    # plt.imshow(cv2.cvtColor(img1_t_cv, cv2.COLOR_BGR2RGB))
    # plt.show()

    for text in strs:
        if(len(text) != 0):
            print(text)

    # print("Recognition completed.")