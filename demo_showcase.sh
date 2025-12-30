#!/bin/bash

# Configuration
BASE_URL="http://localhost:3000"
ADMIN_USER="admin_user"
ADMIN_PASS="admin123"
NORMAL_USER="normal_user"
NORMAL_PASS="user123"

echo "======================================================================"
echo "Starting Demo Showcase for Noosyn Java Demo"
echo "Base URL: $BASE_URL"
echo "======================================================================"
echo ""

# Function to make requests
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local token=$4

    if [ -z "$token" ]; then
        curl -s -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data"
    else
        curl -s -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $token" \
            -d "$data"
    fi
    echo ""
}

# 1. Signup Admin
echo "[1] Signing up Admin User ($ADMIN_USER)..."
make_request "POST" "/api/v1/signup" "{\"username\": \"$ADMIN_USER\", \"password\": \"$ADMIN_PASS\"}" ""
echo "----------------------------------------------------------------------"

# 2. Signin Admin
echo "[2] Signing in Admin User..."
ADMIN_TOKEN=$(curl -s -X POST "$BASE_URL/api/v1/signin" -H "Content-Type: application/json" -d "{\"username\": \"$ADMIN_USER\", \"password\": \"$ADMIN_PASS\"}")
echo "Admin Token received."
echo "----------------------------------------------------------------------"

# 3. Assign ADMIN Role to Admin User
echo "[3] Assigning ADMIN role to $ADMIN_USER..."
make_request "POST" "/api/v1/role_create" "{\"username\": \"$ADMIN_USER\", \"roleName\": \"ADMIN\"}" "$ADMIN_TOKEN"
echo "----------------------------------------------------------------------"

# 4. Signup Normal User
echo "[4] Signing up Normal User ($NORMAL_USER)..."
make_request "POST" "/api/v1/signup" "{\"username\": \"$NORMAL_USER\", \"password\": \"$NORMAL_PASS\"}" ""
echo "----------------------------------------------------------------------"

# 5. Signin Normal User
echo "[5] Signing in Normal User..."
USER_TOKEN=$(curl -s -X POST "$BASE_URL/api/v1/signin" -H "Content-Type: application/json" -d "{\"username\": \"$NORMAL_USER\", \"password\": \"$NORMAL_PASS\"}")
echo "User Token received."
echo "----------------------------------------------------------------------"

# 6. Assign USER Role to Normal User (Using Admin Token)
echo "[6] Assigning USER role to $NORMAL_USER..."
make_request "POST" "/api/v1/role_create" "{\"username\": \"$NORMAL_USER\", \"roleName\": \"USER\"}" "$ADMIN_TOKEN"
echo "----------------------------------------------------------------------"

# 7. Admin Creates Product
echo "[7] Admin creating a product (Laptop)..."
PRODUCT_RESP=$(curl -s -X POST "$BASE_URL/api/v1/product" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -d "{\"name\": \"Laptop\", \"price\": 1200.00}")
echo "Response: $PRODUCT_RESP"
# Extract ID (simple grep/sed hack if jq not available, assuming standard json format)
PRODUCT_ID=$(echo $PRODUCT_RESP | grep -o '"id":[0-9]*' | grep -o '[0-9]*')
echo "Created Product ID: $PRODUCT_ID"
echo "----------------------------------------------------------------------"

# 8. User Gets All Products
echo "[8] User fetching all products..."
curl -s -X GET "$BASE_URL/api/v1/product" \
    -H "Authorization: Bearer $USER_TOKEN"
echo ""
echo "----------------------------------------------------------------------"

# 8.5 User Gets Specific Product
if [ ! -z "$PRODUCT_ID" ]; then
    echo "[8.5] User fetching specific product $PRODUCT_ID..."
    curl -s -X GET "$BASE_URL/api/v1/product/$PRODUCT_ID" \
        -H "Authorization: Bearer $USER_TOKEN"
    echo ""
    echo "----------------------------------------------------------------------"
fi

# 9. User Tries to Create Product (Should Fail - 403)
echo "[9] User trying to create a product (Should Fail)..."
curl -s -X POST "$BASE_URL/api/v1/product" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer " \
    -d "{\"name\": \"Hacker Phone\", \"price\": 0.00}"
echo ""
echo "(Expect 403 Forbidden above)"
echo "----------------------------------------------------------------------"

# 10. Admin Updates Product
if [ ! -z "$PRODUCT_ID" ]; then
    echo "[10] Admin updating product $PRODUCT_ID (Price change)..."
    make_request "PUT" "/api/v1/product/$PRODUCT_ID" "{\"name\": \"Gaming Laptop\", \"price\": 1500.00}" "$ADMIN_TOKEN"
    echo "----------------------------------------------------------------------"

    # 11. Admin Deletes Product
    echo "[11] Admin deleting product $PRODUCT_ID..."
    curl -s -X DELETE "$BASE_URL/api/v1/product/$PRODUCT_ID" \
        -H "Authorization: Bearer $ADMIN_TOKEN"
    echo ""
    echo "Product deleted."
else
    echo "Skipping Update/Delete as Product Creation failed or ID not extracted."
fi
echo "----------------------------------------------------------------------"

echo "Demo Showcase Completed."
