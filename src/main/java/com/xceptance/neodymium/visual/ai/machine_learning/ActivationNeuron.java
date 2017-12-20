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
 * Activation neuron.
 * 
 * @author Diego Catalano edited by Thomas Volkmann
 */
public class ActivationNeuron extends Neuron
{

    /**
     * Value for threshold, which represent the dynamic change value for separation.
     */
    protected double threshold = 0.0;

    /**
     * Interface function value.
     */
    protected IActivationFunction function = null;

    /**
     * Get Threshold value. The value is added to inputs weighted sum before it is passed to activation function.
     * 
     * @return threshold value.
     */
    public double getThreshold()
    {
        return threshold;
    }

    /**
     * get all neurons.
     * 
     * @return neurons ArrayList with all neurons in the layer.
     */
    public ArrayList<Neuron> getNeurons()
    {
        return neurons;
    }

    /**
     * Set Threshold value. The value is added to inputs weighted sum before it is passed to activation function.
     * 
     * @param threshold
     *            Threshold value.
     */
    public void setThreshold(double threshold)
    {
        this.threshold = threshold;
    }

    /**
     * Get Neuron's activation function.
     * 
     * @return Neuron's activation function.
     */
    public IActivationFunction getActivationFunction()
    {
        return function;
    }

    /**
     * Set Neuron's activation function.
     * 
     * @param function
     *            Neuron's activation function.
     */
    public void setActivationFunction(IActivationFunction function)
    {
        this.function = function;
    }

    /**
     * Initializes a new instance of the ActivationNeuron class.
     * 
     * @param function
     *            Neuron's activation function.
     */
    public ActivationNeuron(IActivationFunction function)
    {
        super();
        this.function = function;
        neurons = new ArrayList<>();
    }

    @Override
    public void Randomize()
    {
        super.Randomize();
        this.threshold = r.nextDouble() * (range.length()) + range.getMin();
    }

    public double Compute(int input, int index)
    {
        // initial sum value
        double sum = 0.0;

        sum = neurons.get(index).getWeight() * input;
        sum += threshold;

        // local variable to avoid mutlithreaded conflicts
        double output = function.Function(sum);
        // assign output property as well (works correctly for single threaded usage)
        this.output = output;

        return output;
    }

    /**
     * Auto generated serial number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * All neurons in this layer.
     */
    private ArrayList<Neuron> neurons;

}