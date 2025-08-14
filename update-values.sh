#!/usr/bin/env bash
set -euo pipefail

# ==== Tham số bắt buộc ====
SERVICE_NAME=${1:-}
IMAGE_TAG=${2:-}
ENVIRONMENT=${3:-dev}

if [[ -z "$SERVICE_NAME" || -z "$IMAGE_TAG" ]]; then
    echo "❌ Usage: $0 <SERVICE_NAME> <IMAGE_TAG> [ENVIRONMENT]"
    echo "Ví dụ: ./scripts/update-values.sh bpm-gateway a1b2c3d dev"
    exit 1
fi

# ==== Repo deployment-manifests ====
DEPLOY_REPO=${DEPLOY_REPO:-git@gitlab.com:nguyenngocvien/deployment-manifests.git}

# ==== Clone repo ====
echo "📥 Cloning $DEPLOY_REPO..."
rm -rf deployment-manifests
git clone "$DEPLOY_REPO"
cd deployment-manifests/envs/"$ENVIRONMENT"/"$SERVICE_NAME"

# ==== Cập nhật values.yaml ====
if ! command -v yq >/dev/null 2>&1; then
    echo "⚙️ Cài yq..."
    apk add --no-cache yq || apt-get update && apt-get install -y yq
fi

echo "🔄 Updating image tag for $SERVICE_NAME ($ENVIRONMENT) -> $IMAGE_TAG"
yq e -i ".image.tag = \"$IMAGE_TAG\"" values.yaml

# ==== Commit & Push ====
git config --global user.email "ci@vcorestack.com"
git config --global user.name "CI Orchestrator"
git add values.yaml
git commit -m "ci: update $SERVICE_NAME ($ENVIRONMENT) image to $IMAGE_TAG"
git push origin main

echo "✅ Updated $SERVICE_NAME ($ENVIRONMENT) to $IMAGE_TAG"
