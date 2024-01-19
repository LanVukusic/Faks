# Assignment 3 Instructions

## Ear Recognition Assignment

This assignment focuses on enhancing the recognition component of the biometric pipeline, building on the improved detection phase already covered. It is a mix between the research-focused first assignment and the very-guided second.  
Your task involves either re-implementing or integrating one or more traditional recognition approaches on the dataset you already have (from the second assignment). These should be compared against three established methods: the baseline Local Binary Patterns (LBP) from the first assignment, a pretrained CNN (ResNet50) provided here (you can run predictions on CPU, no need for CUDA), and a third approach of your choice. This third approach can either be a retrained and improved CNN (you need CUDA or Colab though) or another method such as HOG, SIFT, LPQ, SURF, etc. Due to limitations and annoyances you had with Google Colab, training of CNN is therefore optional and feel free to tackle these classical methods.

> The deadline is January 18, 23:59, with the strict cutoff, because assignments need to be graded in time.

## Things you need to do

- Download the code here: <http://tinyurl.com/ibb-assignment-3>

- Run prepare_cropped_ears.py on the dataset from the second assignment to get the dataset easier to use for recognition research.

- Prepare prediction code for the supplied trained CNN. Note: test set involves subjects not present in the training set so CNNs (ResNet50) cannot be directly used for prediction. Instead, you should utilize the CNN as a feature extractor. This involves acquiring the output from the layer preceding the final softmax layer and using this output for recognition task. This is actually the core of this part. However, it is easier than it sounds. It essentially consists of the two lines instead of regular predict:

```py
model.load_state_dict(torch.load('best_model.pt')),  strict=False)
feature_extractor = torch.nn.Sequential(*list(model.children())[:-1])
```

- Using all the images in the test set compute and store these ResNet feature vectors and compute and store the LBP feature vectors.

- Compute recognition performance on these vectors - simply use the code from the first assignment. If you did not complete the first assignment you can use the code from the latest UERC  Toolkit: <http://tinyurl.com/uerc23> to compute performance.

- Now that you have your baselines computed, tackle one (or both) of the following:
  - improve and retrain the supplied train.py ResNet50,
  - integrate/implement one or more of the traditional feature extraction methods such as HOG, SURF, SIFT, LPQ, etc. It is your task to do some research and find implementations and modify/optimize them as needed.

- Report the recognition performance with the measure of your choosing. Note:  
  - Files train.py and dataloader.py are optional files intended to help you if you decide to train your CNN.
  - Assignment will be graded according to the ingenuity and invested effort. E.g.: using plain HOG will yield a small amount of points, serious improvement of e.g. HOG or retraining of some CNN that will significantly outperform the supplied baseline will yield a large amount of points.
