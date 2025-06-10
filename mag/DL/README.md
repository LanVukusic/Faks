# Deep learnign

## Training neural networks

- Function is **linearly seperable** if there exists a hyperplane that splits the inputs in their 2 classes
- **Crossentropy loss** measures how bad is **Q** at describing distrubution **P**. Eg. how much bits of info would you need to explain P if you know Q. If $P=Q$ then 0 bits; loss = 0;
- **KL divergence** Measures how much does P diverge from Q.
- **RMSprop** adaptive learning rate for each parameter based on recent optimizations

## Convnets

- 2D: Images, time-frequency representations
- 1D: Sequential signals (text, audio, time series)
- 3D: Volumetric images, video, 3D grids

- Equivariance to translation

> Equivariance (in this context) means that if the input is shifted (translated), the output shifts by the same amount.  
>Mathematically, for a function ff, equivariance to translation means:  
>$f(shift(x))=shift(f(x))$
>$f(shift(x))=shift(f(x))$  
>where shiftshift is a translation operator (e.g., moving the image to the right by a few pixels).

- **Stride**: Step size of the convolution filter; stride > 1 performs downsampling.
- **Padding**: Extends input borders (usually with zeros) to control output size.
  - **Valid**: No padding, output is smaller.
  - **Same**: Padding preserves input size.

Pooling Layers

- Output depth: Unchanged
- No learnable parameters

### big networks

- **LeNet** LeCun; MNIST prediction. popularises convnets
- **AlexNet**, imagenet, ReLu, 2Gpu training.
  - Network split in two streams was a hadrdware workaround not  anetwork inovation. Used GroupedConvs for splitting.
  - Had large 11x11 conv filters
- **VGG** Had stacked smaller cnns (3x3), improved over AlexNets 11x11. Good model even now - used as a pretrained backbone.
- **Inception / GoogLeNet**
  - **Inception modeules** 1x1 channel reduction conv, 3x3, 5x5, 1x1 parallel convs for different spatial capture. Depth Concat together (concat over channels)
  - **Auxiliarry classifiers**, 2 calssification heads from non final convs to help gradient flow. (Pre resnet approach)
  - Very efficient due to 1x1 channel reduce in inception modules. 10x less params then VGG
  - max pooling + global average pooling

> GAP / **global average pooling** for input $C H W$ computes average over $H x W$ dimmenstions and returns a C long vector.
> Performs flattening better than FC layers, faster, lower param count

- **ResNet**  Core idea are skip connections or residual connections, that make shortucts and sum the outputs.
  - **Residual** idea is that modules learn **residuals** or differences $F(x)=H(x)−x$ and not a dirrect mapping $H(x)$. Shortly modules learn the difference from input $x$ and not the whole representation.
  - Solves the **vanishing gradient** in deep networks, allowing for _VERY_ deep netowkrs (150 +). Direct gradient flow.
  - Some people explain the shortcuts to function as an **ansamble like** architecture due to different paths.
  - No dropout
  - **Wide Resnet** Mega boring.
    - Its not wide at all. ResNext is wide. This thing just $k$ times more channels to all convolutions. thats it.
    - Tackles diminishing feature reuse.
    - Fewer layers (less deep), more feature maps
    - 10x ish faster training
  - **ResNext** (actually a wide net, lmao)
    - Each ResBlock-k, has $k$ parallel convolutions that get merged (summed) at the output
    - implemented by grouped convolutions

> Grouped Convolution  
> Instead of applying a single convolution over all input channels, grouped convolution divides the input channels into multiple disjoint groups.  
> Each group is convolved with its own set of filters.  
> The outputs from all groups are concatenated along the channel dimension.  

- **Inception v4** standardizes the block and removes legacy training limitations. Simplifies layers.
  - Still has different sized convs + depth concat
- **Inception Resnet** Combines the Inception architecture with residual connections (skip connections) inspired by ResNet.
  - Batch norm on all but residual connections
- **Ensamble methods of both were state of the art on imagenet**
- **SeNet** Squeeze and Excite networks.
  - SeNetBlocks use **GAP** to flatten the channels into a vector. Two FC layers learn the importance of each channel and the output is multiplied by the channels, **weighing** each channel by the importance.
- **DenseNet**
  - DenseBlocks have $N$ conv layers, where the convolutions get residual ocnnections from **ALL** preceeding convolutions, densly connecting the block.
  - Between blocks are Transition layers. 1x1 Conv and 2x2 Avg Pool, to reduce dimensions
  - Grouwth rate $r$ controlls channel expension across depth
- **Mobile nets** efficient on device networks
  - Use **depthwise separable convolutions** to decrease computation.
  - efficien channels scaling and rescaling to trade accuracy for efficiency
  - Dpethwise conv + pointwise conv

> Depth wise convolution
> Applies a filter across every channels on its own. (No inter channel communication)

> Pointwise convolution
> Applies 1x1 convolution across all channels mixing the data. (Inter channel communication)

- **Xception (Extreme Inception)** duble downs on the seperable convs
  - All convs are depth -> pointwise convs
  - Uses residual connections
  - Better than inception on larger datasets
  - Hierarchical feature extraction (due to two convolutions)
  - Very efficient (compute wise). Great performance / flop
- **ShuffleNet** uses grouped pointwise convolution for efficiency
  - 13x better than AlexNet on ARM
  - Grouped convolutions are used, to allow communication over a subgroup of channels for efficiency.
  - special **Channel Shuffle** is applied after convs, to randomly rearange the channels, so that next conv will get information from all groups, allowing for inter channel comunication
    - Pointwise group convolution to reduce dimensionality.
    - Channel shuffle to mix channels across groups.
    - Depthwise convolution for spatial filtering.
    - Another pointwise group convolution to restore channel dimensions.
- **NASNet**
  - RL used for model architecure
  - outperforms most of the models in same compute range
  - Normal cells - same output shape
  - Reduction cells - reducesspatial dimm
- **EfficientNet** used grid search to obtain best scaling parameters for **depth, widht, resolution**.
  - Novel approach of **Compound scaling** allows the model to use most efficient combinations of channels, resolutin and width.
  - Intuition Behind Compound Scaling
    - Increasing input resolution demands more layers (depth) to capture larger receptive fields and more ch (width) to capture fine-grained details.
    - Scaling only one dimension (e.g., depth) leads to diminishing returns; uniform scaling balances all dimensions for optimal performance.
  - Avaialble in multiple sizes (B0 - B7) for any compute budget
- **ConvNext** modern transformer based image model
  - Layer norm instead of batch norm
  - Transformer layers / attention
  - Inverted bottleneck $d_{hidden} > d_{input}$
  - Patches -  4×4 convolution with stride 4
    - good for transformer
  - depthwise convolutions and 1×1 convolutions in a ResNeXt-like style
- **Swin** (Shifted Window Transformer)
  - transformer that takes CNN inspiration by hiearchically stacking transformers

## Detection, segmentation, clasification

- **Instance segmentation** asigns instance specific labels in addition to a semantic segmentation mask
- **Panoptic segmentation** assigns a class and an ID to every pixel in the image

### Segmentation models

- **SegNet** conv encoder-decoder
  - uses **max unpooling** (whatever....)
    - During the encoder step, model stores the index of highest value index to restore it to a spatially correct slot in upsampling
- **U-Net** ..u know unet
  - Skip connections across equaly sized encoder - decoder layers.
- **PSP-net** Pyramid Scene Parsing Network
  - 1×1, 2×2, 3×3, and 6×6 pooling operations with different kernel sizes
  - Each pooled output is then upsampled (via interpolation) back to the original feature map size.
  - pyramid pooling module
- **DeepLab** Popularizes **dilated** or **atrous** convolutions
  - Atrous Spatial Pyramid Pooling (ASPP)
    - multiple parallel dilated convolutions with different dilation rates to capture multi-scale context.
    - Fully connected Conditional Random Field (CRF)
      - a graph probabilistic approach. Penalizes neighbours with different classes. Refines the edges.
  - Later versions use depth-wise dilated convs and pointwise conv for merging

### Detection

Classification and localization

Brute force sliding window is expensive.  

- **Regon proposal** approach
  - Generate region proposals and classify with CNN
  - R-CNN
    - Slow, every frame is resized and pushed through CNN
    - Feature extraction CNN
    - Classification with SVM
  - Fast R CNN
    - Fast reagion based convnet
- Faster R-CNN
  - Pretrained (ResNet, VGG) backbone
  - Two stage method. one head for region proposal and one for classification
    - Object score and bbox regression (predicts bbox deltas for tighter crop)
  - **ROI Pooling** (differantiable) process that extracts fixed size regions of interest from a image-wide feature map

### Instance segmentation

- **Mask R-CNN** Instance segmentation network from R-CNN
  - **Mask Prediction** head, besides the BBOX regression and classification heads
  - **ROIAlign** instead of ROI Pooling - better spatial alignment
    - Complex algorithm, that better preserves locations
      - Floating point bboxes, interpolated values in each bin, better accuracy

- **Feature pyramid network** or FPN - Hierarchical feature extractors.
  - Pretrained backbone (ResNet) provides last 5 layers of progressivly smaller feature maps (32x32, 16x16, 8x8, ...), deonted as $C1 \implies C5$
  - Upscaled features $P1 \implies P5$ are computed by upsampling previous P and adding a lateral skip from $C$, U-Net style.
  - (Its just an upside down Unet...lets face it.)
  - It returns all 5 $P$ Features and applies a 3x3 conv is applied for smoothing.

- **Panoptic FPN** FPN + R-CNN
  - Additional semantic segmentation head on top of each pyramid layer
  - Basically apply a R-CNN approach to each layer of FPN
  - a _"panoptic fusion model"_ merges results

- **SSD Single Shot Multibox Detector**
  - Pretrained backbone used for various spatial dimensions
  - Realtime performance
  - Multiscale features
  - Default bbox approach
    - At each scale, at each grid point, define some default, proposed bboxes.
    - Model regresses the bounds and classifies it
  - IOU score
  - Non Maxima supression over scores to find good final candidates

> Intersection over Union - IoU  
> Measure for segmentation, that measures how well is the predicted area covering the GT (intersection) divided by the whole spanning area (Union)  
> We want a good overlap (intersection), but we scale it based on the union of the two.  

- **YOLOs** you only look once, and the 12 versions of it
  - real time object detection
  - 3 scale levels of bbox  prediction
  - predefined anchor boxes (like previous models)
  - Yolo v9
    - **Programmable Gradient Information (PGI)**
      - Additional branch to allow unrestricted and full (pooling, strided convs) backward gradient flow. Removed at inference.
    - **Generalized Efficient Layer Aggregation Network (GELAN**)
      - Dynamic block selection based on hardware
      - Combines cross-stage partial (**CSP**) networks with **ELAN** (Efficient Layer Aggregation Network) principles

> CSP - cross-stage partial network
> Divides block outputs into two parts:
> One part gets processed with further convs
> One part gets bypassed to allow shorter gradient flow
> Both parts concatenated at the end

Both techniques are nothing new and special...

> ELAN - Efficient layer aggregation network
> Basically mixes the channels in ....ways
> 1. Grouped convolutions with channel shuffle
> 2. Multiple convolutions with different depths (e.g., 1×1 conv, stacks of 3×3 convs).

- **RetinaNet**
  - FPN backbone for multi scale features
  - two specific heads attached to each FPN scale:
    - Classification
    - BBOX regression
  - Premade anchor boxes
  - Focal Loss
    - Mitigates class imbalance
    - Focus on the "things" - the foreground elements and give a small weight to "stuff" - the background
  - one stage, fast network

- **CeDir net**
  - Centerpoint and direction net
  - Actually cool
  - Instead of centerpoints predict a vector for each pixel, pointing towards nearest centerpoint.
    - impleneted as sine and coisne channels
  - Robust regression and voting system for centerpoints
  - Can detect objects rotation (orientation adn grasp point usecases, robotics)

## Recurrent nets

Models for use with sequential / temporal data

- **RNN** recurrent neural nets
  - Blocks have internal "memory" that gets updated every step
  - External embeddings (image source, latent, ...) can also be added in the beginning
  - 1-many, many-1, many-many
    - Image captioning, sentance classification, text 2 text
  - Multilayer RNNs
    - Each token goes through many blocks
  - Uses **BPTT** to train

> BPTT - Backprop through time
> Esentially "unrolls" the timesteps into a sequential model, pretending that each timestep is a new layer of the model.
> Backprop is than clasically applied (using chain rule) as if the model was linear.

Due to performance concerns, **Truncated** or **Chunked** packprop through time can be used.
This applies the backprop on a group of consequitve timesteps and saves the inputs and outputs as constants for the next chunk.

Has same problems as very deep neural nets. Vanishing / exploding gradient. Numerical instability:

- gradient clipping
- recursive, making it memory and compute expensive


- **LSTM** Long Short term memory
  - Additional cell state
  - Gates to controll memory - Manages the gradient
    - **Forget gate** sets some parts of the state to 0 ( weigh by sigmoid output)
    - **Input gate** computes new memories and weights them
    - **Output gate** Gets the new state and figurs out what to output
  - Solves BPTT
    - Cell state flow unrestricted
    - Avoids multiple paths through activations - moves the "tricky" activation derivateives out of the way and just adds or multiplies them in.

- **GRU** Gated recurrent Unit
  - 2 gates
    - **Update / keep** controlls what info to store and what to update. returns keep_vector and a 1 - keep vector.
    - **Reset gate** Weighs previous hidden state and discard some info

- **Bidirectional LSTM**
  - Two LSTMs in both ways.
  - Worse (impossible) t generate text
  - Good for retrieval or analysis tasks

- **Attention in RNNS**
  - Attention mechanism is sometimes put inside the cells
  - Context vector is computed and returned

## TRANSFORMERS

A novel architecture for basically anything. Outperforms basically everything.

### Transformer block architecture

- Inputs are fed into **Multi headed attention**
- Normalization layer
- FFC layer (2/3 model weights, responsible for fact storage)
- Normalization layer
  
- Norm layer receive residual connections. Intuition for this is that parts of the model just learn what direction should be added to the multidimensional presentation.

### RNN comparison

| Feature                          | RNNs                                                                 | Transformers                                                                 |
|----------------------------------|----------------------------------------------------------------------|------------------------------------------------------------------------------|
| Long-range dependencies          | Problems with long-range dependencies                                | Facilitate long-range dependencies                                           |
| Gradient issues                 | Vanishing and exploding gradient problems                            | No vanishing and exploding gradient problem                                  |
| Training steps                  | Large number of training steps required                              | Fewer training steps needed                                                  |
| Parallel training            | Recurrence prevents parallel training                             | No recurrence enables parallel training                                   |
| Sequence length                 | Recurrence enables arbitrary sequence * length                         | Fixed and limited sequence length (context fragmentation)                    |
| Pretraining                     | No pretraining is common                                             | Pretraining heavily exploited                                                |
| Multitask capability            | -                                                                    | Multitask models supported                                                   |

\* arbitrary sequence lengths are theoretically possible but require quadratic memmory, so in practice this is not really true

### Models

> **Attention**  
> The main novel idea of the transformer block is the **Attention mechanism**.
> This approach allows variable length sequences to exchange data between eachother.
> Input is split into tokens, where data can be exchanged between them.

- **Encoder - Decoder** models.
  - The model is composed of two (simmilar) parts, encoder and decoder.
  - Encoder processes the input and caches the last embedding
  - Decoder **Autoregresivly** computes the next token from previous tokens and the starting input
  - used for machine translation
  - Dated architecture in favor of decoder only models
  - Data from encoder to decoder is passed via Cross attention

> **Autoregressive generation**  
> The model generates new tokens one after another, by feeding all current toknes into itself recursivly.

- **Transformer XL**
  - segment-level recurrence
    - RNN inspired hidden states that get stored for each segment, allowing model to maintain context over large token ocunts
  - relative positional encoding
    - Instead of encoding a token position it encodes a relative distance from other tokens
    - Fixed and cached hidden states allow paralelized training (unlike rnns)
    - No gradient flow through hidden state
    - Training on one sample allows for in segment paralelization

- **Longformer** transformer architecture for very long contexts
  - Sparse attention
    - Learned or defined mask of tokens is allowed to be attended, not the whole set
    - produces a sparse matrix with many 0 elements which are not computed - saving memory and performance
  - sliding window attention
    - A sliding window allows tokens to compute full $Q^2$ attention locally on a 512x152 grid only
  - Global attention on special tokens [CLS]
    - Global attention is only performed on special tokens
  - Dilated attention
    - Similar to dilated convolution. Same amount of compute for larger area (contex in this example)

> \*extra Locality-Sensitive Hashing (LSH)  
> is a family of hash functions designed so that similar data points (vectors) have a high probability of hashing to the same bucket.
> Dissimilar points hash to different buckets.
> Attention computed between buckets

- **BERT** **Bidirectional** language understanding
  - encoder only
  - Training
    - Random infill task
    - Next token prediction
    - Made for finetuning for different tasks
  - **RoBERTa** robustly trained bert model
    - More data more training
  - **AlBERT** a lightweight bert
    - Downprojection matrix used for scaling vocabulary down, before attention
    - Shares weights across **ALL TRANSFORMER LAYERS** (yes only 1 unique transformer layer actually gets trianed and this works)
    - Training
      - sentence order prediction

- **T5** - Text-To-Text Transfer Transformer
  - encoder decoder
  - All tasks as text 2 text tasks using special _[TASK SPECIFIC TOKEN]_ prefix
  - HUGE dataset 750GB
  - **Flan T5** instruction tuned
    - instructions as an input
      - _"Translate English to German: How are you?"_

- **BART**
  - Bidirectional encoder
  - Autoregressive decoder

### Decoder only models - GPT and company

- Decoder only
- Autoregressive generation
- BPE tokenization (subword byte pair tokens)

GPT1 was shit
GPT 2 was good
GPT3 was bigger and more data and better

**Instruction tuning** in 3 phases

  1. Collect data and train a policy
     1. Random prompts sampled from the database
     2. Expert anotator manually writes the expected output behaviour
     3. Data used for supervised fine tuning
  2. Comparison data to train a reward model
     1. A random prompt is picked and model generates many possible responses
     2. Expert ranks the answers from best to worst
     3. A reward model is trained from this, that can be used to predict rewards based on the model output
  3. RLHF step, where model is optimized with reienforcement learning
     1. Model generates an output and a reward is calculated
     2. Model generates a new output to maximize the policy

RL is needed because the last step is nondiferentiable. We dont have labels for what a good answer would be, but we can judge how well the model performed.


### LLM tuning

Full model training is downright impossible if you are not multi million dollar company  

Model finetuning is still basically impossible because even then the model is too large.

You are stuck with multiple versions of efficient selective fine tuning options:

- **PEFT** parameter efficient fine tuning
  - nothing concrete. Umbrella term for well...efficient tuning
  - uses following techniques
- **Partial model freezing** freezes large percent of the model and just trains a subset
- **Adapters**, inserts additional transformations (Learnable matrices) before or after the FC layers in transformers that can be used to learn new things
  - Initialized as identity
- **LoRa** Low rank adapter.
  - Instead of a whole projection matrix, train two lower rank matrices of custom size (bigger matrix, more parameters, more flexibility, longer training).
  - Two matrices multiply into the correct dimmension, but use fewer parameters than a full matrix

### Vision transformers

- **ViT**
  - Uses convolution to create 16x16 input image patches
  - Patches are linearized and downprojected to an embedding space
  - Learnable patch embeddings are added
  - Patches are treated as tokens and inputed into transformer stack
  - Classification head at the end

> CLS token  
> a special \[CLS] token is used before of other tokens which serves as the aggregate point for all attention down the line
> The idea is, that the model will learn to agregate all important context into this one latent space of the \[CLS] token, which can be used for downstream tasks like classification.

- **DeiT** Data efficient transformer
  - Student Teacher approach
  - [CLS] and [DESTIL] tokens are prepended and two classification heads are constructed
  - First learns to predict ground truth labels, second tries to predict **Teachers labels**.
  - This learns a strong models (ResNet or VGG) biases and the "disagreements" between GT and Teacher act as regularization.
  - Efficient training and inference
  - Can be trained by consumer GPUs

- **MViT** multi scale vision transformer
  - Esentially a pyramid stack of multiscale features shoved into a transformer
  - Pooling attention
  - It works well. shcoker.

- **CvT**
  - At the start of each stage, a convolutional token embedding layer replaces the standard ViT patch embedding.
  - Strong convolutional embeddings with inherent spatial representation
    - NO POSITIONAL EMBEDDINGS - safely removed due to convolutional head
  - Efficient due to depthwise seperable convs

- **Swin**
  - **Hierarchical Design**: Builds multi-scale feature maps by merging patches, similar to CNNs.
  - **Window-based Self-Attention**: Computes attention locally within fixed-size windows for efficiency.
  - **Shifted Windows**: Alternates window positions to enable cross-window connections. moves windows by half, to allow information to cross.  
  - **Patch Merging**: Downsamples spatial resolution while increasing channel dimensions between stages.
  - **Relative Positional Bias**: Adds spatial info to attention without fixed positional embeddings.

Efficient, scalable, and effective for image classification, detection, and segmentation.x¸yy

- **DETR** object detection
  - End-to-end object detection using a CNN backbone + Transformer.  
  - Predicts a fixed set of objects via learnable object queries.  
  - Bipartite matching (Hungarian algorithm) assigns predictions to ground truth.  
  - Eliminates hand-designed components like anchor boxes and NMS.  
  - Loss is **hungarian algorithm**
    - Set based algorithm, that finds minimal cost matching
  - **Object queries** as inputs to decoder part. Esentially slots
    - Learned embeddings input to the Transformer decoder.  
    - Each query predicts a bounding box and class.  
    - Specialize spatially, competing to detect objects.  
    - Enable set-based prediction via bipartite matching.ject queries enable DETR’s end-to-end, set-based object detection by representing potential objects as learned tokens.*
  - **Deformable DT** extension for non rectangualr bboxes
  - **Panoptic segmentation** - each query gets a panoptic head, returns a pixel mask
  - **DINO** contrasitve learning for bbox denoising

- **Mask former** panoptic segmentation network
  - produces a class (mask) embedding
  - high definition (per pixel) embedding
  - Final pixel mask is computed as the dot product between class and pixel embedding.
  - Fixed set of masks
  - Possible overlaping masks

- **Segment anything**
  - Foundation model for segmenting.... anything
  - Takes natural language classes
  - 11M images... this is SO MUCH MANUAL WORK
  - **SAM HQ** trained on difficult objects, additional HQ token. REALLY good results

- **DINOv2**
  - Student teacher approach.
  - Teacher gets the whole global image views, student gets cropped and deformed local image views
  - Student tries to predict the teachers output embeddings
  - Teacher is constructed from the moving average of student (yes its circular, yes it still works)
  - No pretraining
  - **Self supervised**
  - Backbone only
- **Dino X**
  - Unified model for open world object detection + understanding
  - multimodal

- **CLIP** contrastive language / image model
  - merges vision and language

- **RADIO** Agglomerative Vision Foundation Model Reduce All Domains Into One
  - Looks like it outperforms everything on all benchmarks
  - Multi-Teacher Distillation: AM-RADIO uses multiple "teacher" models (like CLIP, DINOv2, and SAM) to train a single "student" model from scratch .

- **SigLip2** better multilingual CLIP


