/* This file was generated by SableCC (http://www.sablecc.org/). */

package sc.node;

import sc.analysis.*;

@SuppressWarnings("nls")
public final class AVraiTypevar extends PTypevar
{
    private TVrai _vrai_;

    public AVraiTypevar()
    {
        // Constructor
    }

    public AVraiTypevar(
        @SuppressWarnings("hiding") TVrai _vrai_)
    {
        // Constructor
        setVrai(_vrai_);

    }

    @Override
    public Object clone()
    {
        return new AVraiTypevar(
            cloneNode(this._vrai_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAVraiTypevar(this);
    }

    public TVrai getVrai()
    {
        return this._vrai_;
    }

    public void setVrai(TVrai node)
    {
        if(this._vrai_ != null)
        {
            this._vrai_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._vrai_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._vrai_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._vrai_ == child)
        {
            this._vrai_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._vrai_ == oldChild)
        {
            setVrai((TVrai) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
