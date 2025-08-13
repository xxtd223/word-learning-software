import time
from tkinter import Image
from flask import Flask, request, jsonify
import requests
import uuid
import random
from transformers import CLIPProcessor, CLIPModel


app = Flask(__name__)

# === 本地配置 ===
COMFYUI_API = "http://127.0.0.1:8188"
# ==================

def generate_client_id():
    """生成唯一的 client_id"""
    return str(uuid.uuid4())

def update_prompt_text(prompt_data, pos, neg):
    """更新正面和负面提示词到 prompt 数据中的 CLIPTextEncode 节点"""
    # 更新正面提示词
    if "6" in prompt_data:
        prompt_data["6"]["inputs"]["text"] = pos

    # 更新负面提示词
    if "7" in prompt_data:
        prompt_data["7"]["inputs"]["text"] = neg

    return prompt_data

def upload_prompt_to_comfyui(prompt_data):
    """上传 prompt 数据到 ComfyUI 的 /prompt API"""
    url = f"{COMFYUI_API}/prompt"  # 使用 /prompt API 端点
    client = requests.Session()

    # 生成唯一的 client_id
    client_id = generate_client_id()

    # 创建上传数据
    json_data = {
        "client_id": client_id,  # 任务ID
        "prompt": prompt_data,  # 将更新后的工作流作为 prompt 参数
    }

    try:
        # 发送 POST 请求
        response = client.post(url, json=json_data)
        if response.status_code == 200:
            return response.json()
        else:
            print(f"❌ 上传工作流失败: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"❌ 请求失败: {e}")
        return None


def generate_random_seed():
    """生成一个15位的随机种子"""
    return random.randint(100000000000000, 999999999999999)


@app.route("/generate", methods=["POST"])
def generate():
    """接收提示词并更新工作流"""
    data = request.get_json()
    if not data:
        return jsonify({"error": "请求必须为 JSON 格式"}), 400

    pos = data.get("positive", "").strip()
    neg = data.get("negative", "").strip()

    if not pos:
        return jsonify({"error": "缺少正面提示词"}), 400

    print(f"✅ 收到提示词: 正面='{pos}'，\n反面='{neg}'")

    # 获取默认的 prompt 数据（邻接表）
    prompt_data = {
  "3": {
    "inputs": {
      "seed": generate_random_seed(),
      "steps": 20,
      "cfg": 6,
      "sampler_name": "euler_ancestral",
      "scheduler": "normal",
      "denoise": 1,
      "model": [
        "11",
        0
      ],
      "positive": [
        "6",
        0
      ],
      "negative": [
        "7",
        0
      ],
      "latent_image": [
        "5",
        0
      ]
    },
    "class_type": "KSampler",
    "_meta": {
      "title": "K采样器"
    }
  },
  "4": {
    "inputs": {
      "ckpt_name": "动漫光影_Anishadow V2.safetensors"
    },
    "class_type": "CheckpointLoaderSimple",
    "_meta": {
      "title": "Checkpoint加载器（简易）"
    }
  },
  "5": {
    "inputs": {
      "width": 512,
      "height": 1448,
      "batch_size": 1
    },
    "class_type": "EmptyLatentImage",
    "_meta": {
      "title": "空Latent图像"
    }
  },
  "6": {
    "inputs": {
      "text": "test",
      "clip": [
        "11",
        1
      ]
    },
    "class_type": "CLIPTextEncode",
    "_meta": {
      "title": "CLIP文本编码"
    }
  },
  "7": {
    "inputs": {
      "text": "test",
      "clip": [
        "4",
        1
      ]
    },
    "class_type": "CLIPTextEncode",
    "_meta": {
      "title": "CLIP文本编码"
    }
  },
  "8": {
    "inputs": {
      "samples": [
        "3",
        0
      ],
      "vae": [
        "4",
        2
      ]
    },
    "class_type": "VAEDecode",
    "_meta": {
      "title": "VAE解码"
    }
  },
  "9": {
    "inputs": {
      "filename_prefix": "20250410",
      "images": [
        "8",
        0
      ]
    },
    "class_type": "SaveImage",
    "_meta": {
      "title": "保存图像"
    }
  },
  "10": {
    "inputs": {
      "lora_name": "MYGO!!!!!（Nagasaki Soyo,Kaname Rāna,heterochromia,Takamatsu Tomori,Shiina Taki,Anon Chihaya).safetensors",
      "strength_model": 1,
      "strength_clip": 1
    },
    "class_type": "LoraLoader",
    "_meta": {
      "title": "加载LoRA"
    }
  },
  "11": {
    "inputs": {
      "lora_name": "4koma.safetensors",
      "strength_model": 1,
      "strength_clip": 1,
      "model": [
        "4",
        0
      ],
      "clip": [
        "4",
        1
      ]
    },
    "class_type": "LoraLoader",
    "_meta": {
      "title": "加载LoRA"
    }
  },
  "13": {
    "inputs": {
      "ckpt_name": "flux1DevHyperNF4Flux1DevBNB_flux1DevHyperNF4.safetensors"
    },
    "class_type": "CheckpointLoaderNF4",
    "_meta": {
      "title": "CheckpointLoaderNF4"
    }
  }
}

    # 更新 prompt 数据中的正面和负面提示词
    updated_prompt_data = update_prompt_text(prompt_data, pos, neg)

    # 上传更新后的 prompt 数据并触发绘图
    result = upload_prompt_to_comfyui(updated_prompt_data)
    if not result:
        return jsonify({"error": "上传工作流失败"}), 500

    return jsonify({
        "message": "✅ 成功提交生成任务",
        "task": result  # 返回生成任务的结果
    })


#model_name = "openai/clip-vit-base-patch32"
#model = CLIPModel.from_pretrained(model_name)
#processor = CLIPProcessor.from_pretrained(model_name)

#def calculate_clip_similarity(image_path, text, torch=None):
#    image = Image.open(image_path)
#    inputs = processor(text=[text], images=image, return_tensors="pt", padding=True)
#
#    with torch.no_grad():
#        outputs = model(**inputs)
#
#    image_features = outputs.image_embeds
#    text_features = outputs.text_embeds
#    similarity = torch.cosine_similarity(image_features, text_features)
#
#   return similarity.item()
#

# 相似度阈值
SIMILARITY_THRESHOLD = 0.7

def generate_image_with_retry(prompt_data, max_retries=3):
    retries = 0

    while retries < max_retries:
        generated_image_path = update_prompt_text(prompt_data,None,None)
        similarity_score = 1
        #similarity_score = calculate_clip_similarity(generated_image_path, prompt_data["positive"])
        print(f"相似度得分: {similarity_score}")

        if similarity_score >= SIMILARITY_THRESHOLD:
            print("✅ 图像符合要求，返回结果")
            return generated_image_path
        else:
            print(f"❌ 相似度低于阈值 ({SIMILARITY_THRESHOLD}), 重试第 {retries + 1} 次")
            retries += 1
            time.sleep(1)
    print(f"❌ 最大重试次数({max_retries})已达，生成的图像不符合要求")

    return None


if __name__ == "__main__":
    # 监听本机 IP 和端口 8001
    app.run(host="0.0.0.0", port=8001)