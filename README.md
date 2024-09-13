# PixShift - Image Processing System

## Objective
Build a system to efficiently process image data from CSV files, performing validation, asynchronous image compression, and database storage.

## Features
1. **CSV Upload:** Accepts a CSV file with the following structure:
   - **Serial Number**
   - **Product Name**
   - **Input Image URLs** (comma-separated)
   
2. **Validation:** Ensures correct CSV formatting.

3. **Asynchronous Image Processing:** Compresses images by 50%.

4. **Database Storage:** Stores both input and compressed image data associated with products.

5. **API Endpoints:**
   - **Upload API:** Submits CSV and returns a unique request ID.
   - **Status API:** Checks image processing status using the request ID.

6. **Bonus:** Webhook integration for notifying after image processing is complete.

## Input CSV Example
| Serial Number | Product Name | Input Image URLs |
|---------------|--------------|------------------|
| 1             | SKU1         | https://image1.jpg, https://image2.jpg |
| 2             | SKU2         | https://image3.jpg, https://image4.jpg |

## Output CSV Example
| Serial Number | Product Name | Input Image URLs                             | Output Image URLs                             |
|---------------|--------------|----------------------------------------------|-----------------------------------------------|
| 1             | SKU1         | https://image1.jpg, https://image2.jpg       | https://image1-output.jpg, https://image2-output.jpg |
| 2             | SKU2         | https://image3.jpg, https://image4.jpg       | https://image3-output.jpg, https://image4-output.jpg |

## Components
- **ImageProcessingService:** Handles image compression.
- **WebhookHandling:** Manages post-processing callbacks.
- **Database:** Tracks request status and stores images.
- **API Endpoints:** Manages CSV upload and status checking.

## API Endpoints
- **POST /upload:** Upload CSV and return request ID.
- **GET /status/{requestId}:** Get processing status by request ID.

## System Design
The system design includes the following components:
1. **Asynchronous Image Processing Service**
2. **Webhook Handling**
3. **Database Interaction for Status Tracking**

A detailed technical design document and a visual diagram of the system (via Draw.io or similar) will be included in the repository, in the future.

## Bonus: Webhook Flow
After processing, the system triggers a webhook to notify the client.
