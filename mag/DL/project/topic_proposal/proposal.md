# Deep learning topic proposal

## Image vectorization using deep learning

I would like to develop and research a tool that I (and potentially thers) would actually benefit from.  

My goal is to train a neural network to read in a raster image and output the SVG of a vectorize image.  
Mainly i would like to focus my attention on the SVG gneration. I would like it to represent a human gnerate vector graphic.
I don't like how current implementations of vectorizers subivide the pixles, create a million of smaller vector shapes and simplify the image.  

### Proposal

I would ingest the image using a (pretrained?) **CNN head** or a **transformer** (if I manage to get enough trining samples). Output would be generated using a language model with limited token space. I would only allow valid SVGs using custom tokenizer and langchain output parser.  

### Data

I believe that vector graphics can be scraped from the internet and raster images can be generated from them.  
Model woul get the reverse task, of translating raster images back to veector ones.

### What would I learn

I would like to try something new, that I have no previous experience on.  
I am very interested in language models that can generate structured data and training them efficiently and i believe that this is a great opportunity for me to try (and fail).  

### Concerns

Since the task as I see it is quite challanging I would kindly ask you if simplyfying my task down the lin would be allowed, if it turns out that model doesnt work as expected.  
Some examples of simplification are listed here:  

- Only outputing black vectors (so skipping any color)
- Training on only a specific set of images (like [Icons](https://tablericons.com/))
- Training without a langchain parser (if the forced structured output turns out to be too complex)
- Using pretrained image embedding heads instead of learning them from scratch
- Only using a specific image size for input images
- ...

Finaly, I'm wondering what success is expected from us. Would it be acceptable if I document the approaches tried even tho none of them worked sucessfully enough?
