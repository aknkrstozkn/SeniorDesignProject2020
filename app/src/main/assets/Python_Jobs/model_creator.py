import tensorflow.compat.v1 as tf_1
import tensorflow as tf
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from tensorflow import SparseTensorSpec
from urllib3.connectionpool import xrange

# tf.compat.v1.disable_eager_execution()



train_file_path = "C:\\Users\\akinkursatozkan\\PycharmProjects\\Final_Attempt\\venv\\train_data.csv"
test_file_path = "C:\\Users\\akinkursatozkan\\PycharmProjects\\Final_Attempt\\venv\\test_data.csv"

column_names = ['BASE_RED', 'BASE_GREEN', 'BASE_BLUE', 'TEST_RED', 'TEST_GREEN', 'TEST_BLUE', 'LABEL']
feature_names = column_names[:-1]
label_name = column_names[-1]

batch_size = 32

train_dataset = tf.data.experimental.make_csv_dataset(
    train_file_path,
    batch_size,
    column_names=column_names,
    label_name=label_name,
    num_epochs=1)

features, labels = next(iter(train_dataset))

test_dataset = tf.data.experimental.make_csv_dataset(
    test_file_path,
    1,
    column_names=column_names,
    label_name=label_name,
    num_epochs=1,
    shuffle=True)

def pack_features_vector(features, labels):
  """Pack the features into a single array."""
  features = tf.stack(list(features.values()), axis=1)
  return features, labels

train_dataset = train_dataset.map(pack_features_vector)
#features = tf.pack([col2, col3, col4, col5, col6, col7, col8, col9, col10, col11])

features, labels = next(iter(train_dataset))

print(features)

model = tf.keras.Sequential([
  tf.keras.layers.Dense(units=64,
                        kernel_initializer=tf.initializers.RandomNormal(stddev=0.01),
                        bias_initializer=tf.initializers.Zeros(), activation=tf.nn.relu, input_shape=(6, )),  # input shape required
  tf.keras.layers.Dense(units=64,
                        kernel_initializer=tf.initializers.RandomNormal(stddev=0.01),
                        bias_initializer=tf.initializers.Zeros(), activation=tf.nn.relu),
  tf.keras.layers.Dense(units=5,
                        kernel_initializer=tf.initializers.RandomNormal(stddev=0.01),
                        bias_initializer=tf.initializers.Zeros(), activation=tf.nn.softmax)
])

predictions = model(features)
#print(predictions[:5])

tf.nn.softmax(predictions)

print("Prediction: {}".format(tf.argmax(predictions, axis=1)))
print("    Labels: {}".format(labels))

loss_object = tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True)



def loss(model, x, y, training):
   # training=training is needed only if there are layers with different
   # behavior during training versus inference (e.g. Dropout).
   y_ = model(x, training=training)

   return loss_object(y_true=y, y_pred=y_)


l = loss(model, features, labels, training=False)
print("Loss test: {}".format(l))

def grad(model, inputs, targets):
  with tf.GradientTape() as tape:
    loss_value = loss(model, inputs, targets, training=True)
  return loss_value, tape.gradient(loss_value, model.trainable_variables)

optimizer = tf.keras.optimizers.SGD(learning_rate=0.01)
loss_value, grads = grad(model, features, labels)

print("Step: {}, Initial Loss: {}".format(optimizer.iterations.numpy(),
                                          loss_value.numpy()))

optimizer.apply_gradients(zip(grads, model.trainable_variables))

print("Step: {},         Loss: {}".format(optimizer.iterations.numpy(),
                                          loss(model, features, labels, training=True).numpy()))

## Note: Rerunning this cell uses the same model variables

# Keep results for plotting
train_loss_results = []
train_accuracy_results = []

num_epochs = 201

for epoch in range(num_epochs):
  epoch_loss_avg = tf.keras.metrics.Mean()
  epoch_accuracy = tf.keras.metrics.SparseCategoricalAccuracy()

  # Training loop - using batches of 32
  for x, y in train_dataset:
    # Optimize the model
    loss_value, grads = grad(model, x, y)
    optimizer.apply_gradients(zip(grads, model.trainable_variables))

    # Track progress
    epoch_loss_avg.update_state(loss_value)  # Add current batch loss
    # Compare predicted label to actual label
    # training=True is needed only if there are layers with different
    # behavior during training versus inference (e.g. Dropout).
    epoch_accuracy.update_state(y, model(x, training=True))

  # End epoch
  train_loss_results.append(epoch_loss_avg.result())
  train_accuracy_results.append(epoch_accuracy.result())

  if epoch % 50 == 0:
    print("Epoch {:03d}: Loss: {:.3f}, Accuracy: {:.3%}".format(epoch,
                                                                epoch_loss_avg.result(),
                                                                epoch_accuracy.result()))

test_dataset = test_dataset.map(pack_features_vector)
test_accuracy = tf.keras.metrics.Accuracy()

logits = None
x_test = None
y_test = None
prediction = None
for (x, y) in test_dataset:
  # training=False is needed only if there are layers with different
  # behavior during training versus inference (e.g. Dropout).
  print("X = {}".format(x))
  print("Y = {}".format(y))
  x_test = x
  y_test = y
  logits = model(x, training=False)
  prediction = tf.argmax(logits, axis=1, output_type=tf.int32)
  test_accuracy(prediction, y)

print("Test set accuracy: {:.3%}".format(test_accuracy.result()))

print("test input: {}".format(x_test))
print("test inputs label {}".format(y_test))
print("output as logit: {}".format(logits))
print("output as predicted label : {}".format(prediction))


model.save('my_model')

