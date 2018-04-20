// Catalano Neuro Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//
// Copyright 2017 Thomas Volkmann
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this
// software and associated documentation files (the "Software"), 
// to deal in the Software without restriction, including without limitation the rights 
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
// and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all 
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
// BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.xceptance.neodymium.visual.ai.machine_learning;

import java.util.ArrayList;

/**
 * Perceptron learning algorithm.
 * 
 * @author Diego Catalano edited by Thomas Volkmann
 */
public class PerceptronLearning implements IUnSupervisedLearning
{
    // network to teach
    private ActivationNetwork network;

    // learning rate
    private double learningRate = 0.1;

    /**
     * Get Learning rate. Range[0, 1].
     * 
     * @return Learning rate.
     */
    public double getLearningRate()
    {
        return learningRate;
    }

    /**
     * Set Learning rate. Range[0, 1].
     * 
     * @param learningRate
     *            Learning rate.
     */
    public void setLearningRate(double learningRate)
    {
        this.learningRate = Math.max(0.0, Math.min(1.0, learningRate));
    }

    /**
     * Initializes a new instance of the PerceptronLearning class.
     * 
     * @param network
     *            Network to teach.
     */
    public PerceptronLearning(ActivationNetwork network)
    {
        this.network = network;
    }

    @Override
    public double run(ArrayList<Integer> input)
    {
        // get the only layer of the network
        Layer layer = network.layer;

        if (input.size() != layer.getActivationNeuron().getNeurons().size())
        {
            layer.inputsCount = input.size();
            while (layer.getActivationNeuron().getNeurons().size() < input.size())
            {
                layer.getActivationNeuron().getNeurons().add(new Neuron());
            }
        }

        // compute output of network
        ArrayList<Double> networkOutput = network.Compute(input);
        // counter for misclassified prediction of the network
        int misclassified = 0;
        // summary network absolute error
        double error = 0.0;
        // check output of each neuron and update weights
        for (int j = 0; j < input.size(); j++)
        {
            double er = input.get(j) - networkOutput.get(j);
            // if the prediction of the network was right no learning is necessary
            // Math.floor(e * 10) / 10 for less precision
            if (Math.floor(er * 100) / 100 != 0)
            {
                // update weights
                layer.activeNeuron.getNeurons().get(j)
                                  .setWeight(layer.activeNeuron.getNeurons().get(j).weight + learningRate * er * (input.get(j) == 0 ? -0.2 : 1));
                // update threshold value
                layer.activeNeuron.setThreshold(layer.activeNeuron.getThreshold() + learningRate * er);
                ++misclassified;
            }
            // make error to be absolute
            error += Math.abs(er);
        }
        return error / misclassified;
    }
}