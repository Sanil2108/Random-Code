import cv2
import matplotlib.pyplot as plt
import numpy as np

im = cv2.imread('images/image5.png')

# plt.subplots(figsize=(60, 60))

im_resized = cv2.resize(im, None, fx=1, fy=1)

edged_im= cv2.Canny(im_resized, 10, 10)

im_bw = cv2.cvtColor(edged_im, cv2.COLOR_BGR2RGB)
im_bw = cv2.cvtColor(im_bw, cv2.COLOR_RGB2GRAY)

# cv2.imwrite("im_temp.png", im_bw)

# plt.imshow(im_bw)

def grid_info(im_bw):
    row_decided = 500
    col_decided = 500
    row_edges = 0
    col_edges = 0
    
    row_start = -1
    row_end = -1
    col_start = -1
    col_end = -1
    
    row_block_size = []
    col_block_size = []
    
    for i in range(len(im_bw[row_decided])):
        if row_edges == 0:
            row_start = i
        if im_bw[row_decided,i] > 0:
            row_edges += 1
            row_end = i
            
    for i in range(len(im_bw[:, col_decided])):
        if col_edges == 0:
            col_start = i
        if im_bw[i, col_decided] > 0:
            col_edges += 1
            col_end = i
            
    width = len(im_bw[0])   
    height = len(im_bw)
    
    focused_image = im_bw[col_start:col_end, row_start:row_end]
            
    final_dict = {'row_edge_count':int(row_edges/2), 
                  'col_edge_count':int(col_edges/2), 
                  'row_start':row_start,
                  'col_start':col_start,
                  'row_end':row_end,
                  'col_end':col_end,
                  'focused_image':focused_image
                 }
    return final_dict



def get_blocks(numbers_image, grid_info, blocks_image):
    block_width = len(numbers_image[0])/grid_info['row_edge_count']
    block_height = len(numbers_image)/grid_info['col_edge_count']
    
    blocks = []
    
    for i in range(grid_info['col_edge_count']):
        blocks_temp = []
        for j in range(grid_info['row_edge_count']):
            x = i*int(block_width)
            y = j*int(block_height)
            block = numbers_image[x:x+int(block_width), y:y+int(block_height)]
            blocks_temp.append(block)
        blocks.append(blocks_temp)
    
    plt.subplots(figsize=(20, 20))
    plt.imshow(blocks[6][0])
    
    print(len(blocks), len(blocks[0]))
    
    print (block_width, block_height)


grid_dict = grid_info(im_bw)
# print(grid_dict)
focused_im = grid_dict['focused_image']
# plt.imshow(focused_im)

numbers_image = cv2.imread('images/image1.png')
numbers_image = numbers_image[grid_dict['col_start']:grid_dict['col_end'], grid_dict['row_start']:grid_dict['row_end']]

numbers_image = cv2.cvtColor(numbers_image, cv2.COLOR_BGR2RGB)
numbers_image = cv2.cvtColor(numbers_image, cv2.COLOR_RGB2GRAY)

cv2.imwrite('numbers.png', numbers_image)
                              
plt.imshow(numbers_image)
get_blocks(numbers_image, grid_dict, focused_im)